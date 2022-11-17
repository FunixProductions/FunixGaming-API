package fr.funixgaming.api.server.external_api_impl.twitch.auth.clients;

import feign.Headers;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.dtos.TwitchTokenResponseDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.dtos.TwitchValidationTokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(
        name = "TwitchTokenAuthClient",
        url = "${twitch.api.app-auth-domain-url}"
)
public interface TwitchTokenAuthClient {

    /**
     * <a href="https://dev.twitch.tv/docs/authentication/getting-tokens-oauth#client-credentials-grant-flow">Documentation</a>
     * @param formParams map where you need to have client_id client_secret grant_type
     * @return access token
     */
    @PostMapping("/oauth2/token")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    TwitchTokenResponseDTO getToken(Map<String, String> formParams);

    /**
     * <a href="https://dev.twitch.tv/docs/authentication/validate-tokens">Doc</a>
     * @param oAuthToken must start with OAuth
     * @return user infos or 401 of token revoked
     */
    @GetMapping("/oauth2/validate")
    TwitchValidationTokenResponseDTO validateToken(@Header(name = HttpHeaders.AUTHORIZATION) String oAuthToken);
}
