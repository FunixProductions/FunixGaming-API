package fr.funixgaming.api.client.config;

import feign.FeignException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.okhttp.OkHttpClient;
import fr.funixgaming.api.client.user.clients.UserAuthClient;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.core.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

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
            log.error("Une erreur est survenue lors de l'auth pour la funix api. Erreur: {}", e.getMessage());
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
        try {
            final UserTokenDTO token = this.tokenCache;
            this.userAuthClient.valid(token.getToken());

            return true;
        } catch (FeignException e) {
            return false;
        }
    }

    private UserTokenDTO generateNewToken() throws ApiException {
        try {
            final UserLoginDTO loginDTO = new UserLoginDTO();
            loginDTO.setUsername(this.funixApiConfig.getUserApiUsername());
            loginDTO.setPassword(this.funixApiConfig.getUserApiPassword());

            return this.userAuthClient.login(loginDTO, null);
        } catch (FeignException e) {
            throw new ApiException(String.format("Erreur génération token. Erreur code: %s message: %s", e.status(), e.contentUTF8()));
        }
    }

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
}
