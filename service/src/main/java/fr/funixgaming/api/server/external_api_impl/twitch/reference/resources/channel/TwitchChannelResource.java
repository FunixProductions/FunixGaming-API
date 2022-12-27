package fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.channel;

import fr.funixgaming.api.client.external_api_impl.twitch.auth.dtos.TwitchClientTokenDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.auth.enums.TwitchClientTokenType;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.clients.channel.TwitchChannelClient;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.requests.TwitchChannelUpdateDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.channel.TwitchChannelDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelUserDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchClientTokenService;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.TwitchReferenceResource;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.channel.TwitchReferenceChannelService;
import fr.funixgaming.api.server.user.services.UserCrudService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/twitch/channel")
public class TwitchChannelResource extends TwitchReferenceResource implements TwitchChannelClient {

    private final TwitchReferenceChannelService service;

    public TwitchChannelResource(UserCrudService userCrudService,
                                 TwitchClientTokenService tokenService,
                                 TwitchReferenceChannelService service) {
        super(userCrudService, tokenService);
        this.service = service;
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelDTO> getChannelInformation(List<String> broadcasterId) {
        return service.getChannelInformation(
                super.getTwitchAuthByUserConnected(TwitchClientTokenType.VIEWER).getAccessToken(),
                broadcasterId
        );
    }

    @Override
    public void updateChannelInformation(TwitchChannelUpdateDTO channelUpdateDTO) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected(TwitchClientTokenType.STREAMER);

        service.updateChannelInformation(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId(),
                channelUpdateDTO
        );
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelUserDTO> getChannelVips(String maximumReturned, String after, List<String> userIds) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected(TwitchClientTokenType.STREAMER);

        return service.getChannelVips(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId(),
                maximumReturned,
                after,
                userIds
        );
    }
}
