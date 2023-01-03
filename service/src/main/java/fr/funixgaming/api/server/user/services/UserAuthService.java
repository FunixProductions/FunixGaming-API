package fr.funixgaming.api.server.user.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserSecretsDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.utils.network.IPUtils;
import fr.funixgaming.api.server.user.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private static final int MAX_ATTEMPT = 8;
    private static final int COOLDOWN_REQUEST_SPAM = 15;

    private final AuthenticationManager authenticationManager;
    private final IPUtils ipUtils;

    private final UserCrudService userCrudService;
    private final UserTokenService tokenService;

    private final LoadingCache<String, Integer> triesCache = CacheBuilder.newBuilder()
            .expireAfterWrite(COOLDOWN_REQUEST_SPAM, TimeUnit.MINUTES).build(new CacheLoader<>() {
                @Override
                @NonNull
                public Integer load(@NonNull String s) {
                    return 0;
                }
            });

    @Transactional
    public UserDTO register(final UserCreationDTO userCreationDTO) {
        if (userCreationDTO.getPassword().equals(userCreationDTO.getPasswordConfirmation())) {
            final UserSecretsDTO userSecretsDTO = userCrudService.getMapper().toSecretsDto(userCreationDTO);
            userSecretsDTO.setRole(UserRole.USER);

            return userCrudService.create(userSecretsDTO);
        } else {
            throw new ApiBadRequestException("Les mots de passe ne correspondent pas.");
        }
    }

    @Transactional
    public UserTokenDTO login(UserLoginDTO request, HttpServletRequest servletRequest) {
        if (isBlocked(servletRequest)) {
            throw new ApiForbiddenException("Vous avez fait trop d'essais pour vous connecter. Votre accès est temporairement bloqué.");
        }

        try {
            final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            final Authentication authenticate = authenticationManager.authenticate(auth);

            if (authenticate.getPrincipal() instanceof final User user) {
                triesCache.invalidate(ipUtils.getClientIp(servletRequest));
                return tokenService.generateAccessToken(user, request.getStayConnected());
            } else {
                throw new ApiException("Une erreur interne est survenue lors de la connexion.");
            }
        } catch (BadCredentialsException ex) {
            failLogin(servletRequest);
            throw new ApiBadRequestException("Vos identifiants sont incorrects.", ex);
        }
    }

    private boolean isBlocked(final HttpServletRequest servletRequest) {
        return triesCache.getUnchecked(ipUtils.getClientIp(servletRequest)) >= MAX_ATTEMPT;
    }

    private void failLogin(final HttpServletRequest servletRequest) {
        final String key = ipUtils.getClientIp(servletRequest);

        int attempts = triesCache.getUnchecked(key);
        triesCache.put(key, attempts + 1);
    }

}