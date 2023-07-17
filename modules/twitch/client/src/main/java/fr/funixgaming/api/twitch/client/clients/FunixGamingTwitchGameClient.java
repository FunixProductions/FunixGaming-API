package fr.funixgaming.api.twitch.client.clients;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.game.TwitchGameDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "FunixGamingTwitchGameClient",
        url = "${funixgaming.api.twitch.app-domain-url}",
        path = "/twitch/games"
)
public interface FunixGamingTwitchGameClient {

    @GetMapping("name")
    TwitchDataResponseDTO<TwitchGameDTO> getGameByName(@RequestParam(name = "name") String name);

    @GetMapping("id")
    TwitchDataResponseDTO<TwitchGameDTO> getGameById(@RequestParam(name = "id") String id);

}
