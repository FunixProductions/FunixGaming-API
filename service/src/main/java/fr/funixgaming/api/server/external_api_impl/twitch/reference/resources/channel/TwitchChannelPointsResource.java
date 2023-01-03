package fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.channel;

import fr.funixgaming.api.client.external_api_impl.twitch.auth.dtos.TwitchClientTokenDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.auth.enums.TwitchClientTokenType;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.clients.channel.TwitchChannelPointsClient;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelRewardDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchClientTokenService;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.TwitchReferenceResource;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.channel.TwitchReferenceChannelPointsService;
import fr.funixgaming.api.server.user.services.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/channel/custom_rewards")
public class TwitchChannelPointsResource extends TwitchReferenceResource implements TwitchChannelPointsClient {

    private final TwitchReferenceChannelPointsService service;

    public TwitchChannelPointsResource(CurrentSession currentSession,
                                       TwitchClientTokenService tokenService,
                                       TwitchReferenceChannelPointsService service) {
        super(tokenService, currentSession);
        this.service = service;
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelRewardDTO> getChannelRewards() {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected(TwitchClientTokenType.STREAMER);

        return service.getChannelRewards(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId()
        );
    }
}
