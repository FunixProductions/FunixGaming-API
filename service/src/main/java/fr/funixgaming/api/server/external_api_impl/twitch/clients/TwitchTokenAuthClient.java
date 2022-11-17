package fr.funixgaming.api.server.external_api_impl.twitch.clients;

import feign.Headers;
import fr.funixgaming.api.server.external_api_impl.twitch.dtos.TwitchServerTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
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
    TwitchServerTokenDTO getToken(Map<String, String> formParams);

}
