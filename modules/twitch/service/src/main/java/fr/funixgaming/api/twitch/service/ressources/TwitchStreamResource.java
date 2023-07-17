package fr.funixgaming.api.twitch.service.ressources;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream.TwitchStreamDTO;
import fr.funixgaming.api.twitch.client.clients.FunixGamingTwitchStreamClient;
import fr.funixgaming.api.twitch.service.services.TwitchStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/stream")
@RequiredArgsConstructor
public class TwitchStreamResource implements FunixGamingTwitchStreamClient {

    private final TwitchStreamService service;

    @Override
    public TwitchDataResponseDTO<TwitchStreamDTO> getStream() {
        return service.fetchFunixStreamData();
    }
}
