package fr.funixgaming.api.twitch.client.clients;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream.TwitchStreamDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "TwitchStreamClient",
        url = "${funixgaming.api.twitch.app-domain-url}",
        path = "/twitch/stream"
)
public interface TwitchStreamClient {

    @GetMapping
    TwitchDataResponseDTO<TwitchStreamDTO> getStream();

}
