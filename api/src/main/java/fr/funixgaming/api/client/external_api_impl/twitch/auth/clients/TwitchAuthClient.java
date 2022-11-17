package fr.funixgaming.api.client.external_api_impl.twitch.auth.clients;

import fr.funixgaming.api.client.external_api_impl.twitch.auth.dtos.TwitchClientTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchAuthClient",
        url = "${funix.api.app-domain-url}",
        path = "/twitch/auth/"
)
public interface TwitchAuthClient {

    @GetMapping("clientAuthUrl")
    String getAuthClientUrl(@RequestParam(defaultValue = "VIEWER") String tokenType);

    @GetMapping("accessToken")
    TwitchClientTokenDTO getAccessToken(@RequestParam(defaultValue = "VIEWER") String tokenType);

}
