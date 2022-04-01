package fr.funixgaming.api.server.user.services;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.server.configs.FunixApiConfig;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.entities.UserToken;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService extends ApiService<UserDTO, User, UserMapper, UserRepository> implements UserDetailsService {
    private final static String ISSUER = "FunixApi - api.funixgaming.fr";

    private final FunixApiConfig funixApiConfig;
    private final UserTokenRepository tokenRepository;
    private final UserTokenMapper tokenMapper;
    private final UserAuthMapper authMapper;

    public UserService(UserRepository repository,
                       UserMapper mapper,
                       FunixApiConfig funixApiConfig,
                       UserTokenRepository userTokenRepository,
                       UserTokenMapper userTokenMapper,
                       UserAuthMapper userAuthMapper) {
        super(repository, mapper);
        this.funixApiConfig = funixApiConfig;
        this.tokenRepository = userTokenRepository;
        this.tokenMapper = userTokenMapper;
        this.authMapper = userAuthMapper;
    }

    @Transactional
    public UserDTO create(final UserCreationDTO userCreationDTO) {
        final Optional<User> search = this.getRepository().findByUsername(userCreationDTO.getUsername());

        if (search.isPresent()) {
            throw new ApiBadRequestException(String.format("L'utilisateur %s existe déjà.", userCreationDTO.getUsername()));
        } else {
            final User request = this.authMapper.toEntity(userCreationDTO);
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
                .signWith(SignatureAlgorithm.HS512, funixApiConfig.getSecret())
                .compact());

        return tokenMapper.toDto(tokenRepository.save(userToken));
    }

    public boolean isTokenValid(final String token) {
        try {
            Jwts.parser().setSigningKey(funixApiConfig.getSecret()).parseClaimsJws(token);
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
                .setSigningKey(funixApiConfig.getSecret())
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
}
