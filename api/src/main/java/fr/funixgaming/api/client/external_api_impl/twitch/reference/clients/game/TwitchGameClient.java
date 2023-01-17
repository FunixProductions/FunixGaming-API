package fr.funixgaming.api.client.external_api_impl.twitch.reference.clients.game;

import fr.funixgaming.api.client.config.FeignConfig;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.game.TwitchGameDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchGameClient",
        url = "${funix.api.app-domain-url}",
        path = "twitch/game",
        configuration = FeignConfig.class
)
public interface TwitchGameClient {

    @GetMapping("name")
    TwitchDataResponseDTO<TwitchGameDTO> getGameByName(@RequestParam(name = "name") String name);

    @GetMapping("id")
    TwitchDataResponseDTO<TwitchGameDTO> getGameById(@RequestParam(name = "id") String id);

}
