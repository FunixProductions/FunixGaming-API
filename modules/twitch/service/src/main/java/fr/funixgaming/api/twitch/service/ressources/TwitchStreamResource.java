package fr.funixgaming.api.twitch.service.ressources;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream.TwitchStreamDTO;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.twitch.client.clients.FunixGamingTwitchStreamClient;
import fr.funixgaming.api.twitch.service.services.DrakkadesTwitchStreamService;
import fr.funixgaming.api.twitch.service.services.FunixGamingTwitchStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/stream")
@RequiredArgsConstructor
public class TwitchStreamResource implements FunixGamingTwitchStreamClient {

    private final FunixGamingTwitchStreamService funixTwitchStreamService;
    private final DrakkadesTwitchStreamService drakkadesTwitchStreamService;

    @Override
    public TwitchDataResponseDTO<TwitchStreamDTO> getStream(String channel) {
        if (channel.equals("funixgaming")) {
            return funixTwitchStreamService.getCacheStream();
        } else if (channel.equals("drakkades")) {
            return drakkadesTwitchStreamService.getCacheStream();
        } else {
            throw new ApiBadRequestException("Le nom du stream est invalide ou ne fait pas parti de la liste des streams autorisés.");
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelChattersDTO> getChatters(String channel) {
        return funixTwitchStreamService.getCacheChatters();
    }
}
