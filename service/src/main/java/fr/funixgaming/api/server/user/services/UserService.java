package fr.funixgaming.api.server.user.services;

import fr.funixgaming.api.client.user.dtos.requests.UserAdminDTO;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import fr.funixgaming.api.core.utils.encryption.Encryption;
import fr.funixgaming.api.core.utils.network.IPUtils;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.entities.UserToken;
import fr.funixgaming.api.server.user.mappers.UserAdminMapper;
import fr.funixgaming.api.server.user.mappers.UserAuthMapper;
import fr.funixgaming.api.server.user.mappers.UserMapper;
import fr.funixgaming.api.server.user.mappers.UserTokenMapper;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import fr.funixgaming.api.server.user.repositories.UserTokenRepository;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
public class UserService extends ApiService<UserDTO, User, UserMapper, UserRepository> implements UserDetailsService {
    private final static String ISSUER = "FunixApi - api.funixgaming.fr";

    private final UserTokenRepository tokenRepository;
    private final UserTokenMapper tokenMapper;
    private final UserAuthMapper authMapper;
    private final UserAdminMapper adminMapper;
    private final Encryption encryption;
    private final IPUtils ipUtils;

    public UserService(UserRepository repository,
                       UserMapper mapper,
                       Encryption encryption,
                       UserTokenRepository userTokenRepository,
                       UserTokenMapper userTokenMapper,
                       UserAdminMapper adminMapper,
                       UserAuthMapper userAuthMapper,
                       IPUtils ipUtils) {
        super(repository, mapper);
        this.encryption = encryption;
        this.tokenRepository = userTokenRepository;
        this.tokenMapper = userTokenMapper;
        this.authMapper = userAuthMapper;
        this.adminMapper = adminMapper;
        this.ipUtils = ipUtils;
    }

    @Transactional
    public UserDTO create(final UserCreationDTO userCreationDTO) {
        final Optional<User> search = this.getRepository().findByUsername(userCreationDTO.getUsername());

        if (search.isPresent()) {
            throw new ApiBadRequestException(String.format("L'utilisateur %s existe déjà.", userCreationDTO.getUsername()));
        } else {
            final User request = this.authMapper.toEntity(userCreationDTO);

            //request.setPassword(passwordEncoder.encode(request.getPassword()));
            return this.getMapper().toDto(this.getRepository().save(request));
        }

    }

    @Nullable
    public UserDTO getCurrentUser() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            return null;
        }

        final Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return super.getMapper().toDto((User) principal);
        }
        return null;
    }

    @Transactional
    public UserTokenDTO generateAccessToken(final User user) {
        final Instant now = Instant.now();
        final Instant expiresAt = now.plusSeconds(604800); //one week
        final UserToken userToken = new UserToken();

        userToken.setUser(user);
        userToken.setUuid(UUID.randomUUID());
        userToken.setExpirationDate(Date.from(expiresAt));
        userToken.setToken(Jwts.builder()
                .setSubject(userToken.getUuid().toString())
                .setIssuer(ISSUER)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .signWith(SignatureAlgorithm.HS512, encryption.getKey())
                .compact());

        return tokenMapper.toDto(tokenRepository.save(userToken));
    }

    @Transactional
    public void invalidTokens(final UUID userUUID) {
        final Optional<User> search = this.getRepository().findByUuid(userUUID.toString());

        if (search.isPresent()) {
            final User user = search.get();
            final Set<UserToken> tokens = user.getTokens();

            this.tokenRepository.deleteAll(tokens);
        }
    }

    public boolean isTokenValid(final String token) {
        try {
            Jwts.parser().setSigningKey(encryption.getKey()).parseClaimsJws(token);
            final UserToken userToken = getToken(token);

            if (userToken == null) {
                throw new ApiBadRequestException("Votre token d'accès est invalide.");
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public UsernamePasswordAuthenticationToken authenticateToken(final String token) {
        final UserToken userToken = this.getToken(token);
        final User user;

        if (userToken == null) {
            user = null;
        } else {
            user = userToken.getUser();
        }

        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                user == null ? List.of() : user.getAuthorities()
        );
    }

    @Nullable
    private UserToken getToken(final String token) {
        final Claims claims = Jwts.parser()
                .setSigningKey(encryption.getKey())
                .parseClaimsJws(token)
                .getBody();

        final String tokenUuid = claims.getSubject();
        final Optional<UserToken> search = tokenRepository.findByUuid(tokenUuid);

        if (search.isPresent()) {
            return search.get();
        } else {
            return null;
        }
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return super.getRepository()
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("Utilisateur %s non trouvé", username))
                );
    }

    @Transactional
    public UserDTO create(UserAdminDTO request) {
        final User creation = this.adminMapper.toEntity(request);
        return this.getMapper().toDto(getRepository().save(creation));
    }

    @Transactional
    public UserDTO update(UserAdminDTO request) {
        final UserAdminDTO adminDTO = ApiService.patch(request, adminMapper, getRepository());

        if (adminDTO != null) {
            invalidTokens(request.getId());

            return adminDTO;
        } else {
            throw new ApiNotFoundException(String.format("L'utilisateur id %s n'existe pas.", request.getId()));
        }
    }

    @Override
    public void delete(String id) {
        super.delete(id);
    }

    public void checkWhitelist(final String ip, final String username) {
        if (username.equalsIgnoreCase("api") && !ipUtils.canAccess(ip)) {
            throw new ApiForbiddenException("Vous n'êtes pas whitelisté.");
        }
    }
}
