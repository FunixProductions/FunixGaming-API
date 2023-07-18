package fr.funixgaming.api.twitch.client.clients;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream.TwitchStreamDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchStreamClient",
        url = "${funixgaming.api.twitch.app-domain-url}",
        path = "/twitch/stream"
)
public interface FunixGamingTwitchStreamClient {

    @GetMapping
    TwitchDataResponseDTO<TwitchStreamDTO> getStream(@RequestParam(defaultValue = "funix") String channel);

    @GetMapping("chatters")
    TwitchDataResponseDTO<TwitchChannelChattersDTO> getChatters(@RequestParam(defaultValue = "funix") String channel);

}
