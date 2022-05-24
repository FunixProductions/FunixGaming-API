package fr.funixgaming.api.client.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import fr.funixgaming.api.client.user.clients.UserAuthClient;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.core.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Slf4j
public class FunixApiAuthConfig implements RequestInterceptor {

    private final FunixApiConfig funixApiConfig;
    private final UserAuthClient userAuthClient;

    private UserTokenDTO tokenCache;

    public FunixApiAuthConfig(FunixApiConfig funixApiConfig,
                              UserAuthClient userAuthClient) {
        this.funixApiConfig = funixApiConfig;
        this.userAuthClient = userAuthClient;
    }

    @Override
    public void apply(final RequestTemplate template) {
        try {
            final UserTokenDTO token = getToken();
            template.header("Authorization", String.format("Bearer %s", token.getToken()));
        } catch (ApiException e) {
            log.error("Une erreur est survenue lors de l'auth pour la funix api. Erreur: {}", e.getMessage(), e);
        }
    }

    @NonNull
    private UserTokenDTO getToken() throws ApiException {
        final UserTokenDTO tokenDTO;

        if (isTokenValid()) {
            tokenDTO = this.tokenCache;
        } else {
            tokenDTO = generateNewToken();
        }

        if (tokenDTO == null) {
            throw new ApiException("Le token d'accès à la funix api est invalide. Veuillez vérifier les identifiants de connexion.");
        } else {
            this.tokenCache = tokenDTO;
            return tokenDTO;
        }
    }

    private boolean isTokenValid() {
        final Date now = Date.from(Instant.now().minusSeconds(60));

        if (this.tokenCache == null || this.tokenCache.getExpirationDate().before(now)) {
            return false;
        } else {
            return isTokenUsable();
        }
    }

    private boolean isTokenUsable() {
        final UserTokenDTO token = this.tokenCache;
        final ResponseEntity<Void> response = this.userAuthClient.valid(token.getToken());

        return response.getStatusCode().is2xxSuccessful();
    }

    private UserTokenDTO generateNewToken() throws ApiException {
        final UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername(this.funixApiConfig.getUserApiUsername());
        loginDTO.setPassword(this.funixApiConfig.getUserApiPassword());

        final ResponseEntity<UserTokenDTO> response = this.userAuthClient.login(loginDTO);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new ApiException(String.format("Erreur de connexion. Code erreur %d", response.getStatusCodeValue()));
        }
    }
}
