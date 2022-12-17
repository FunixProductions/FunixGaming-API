package fr.funixgaming.api.server.external_api_impl.twitch.reference.services.channel;

import feign.FeignException;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelRewardDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.channel.TwitchReferenceChannelPointsClient;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.TwitchReferenceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwitchReferenceChannelPointsService extends TwitchReferenceService implements TwitchReferenceChannelPointsClient {

    private final TwitchReferenceChannelPointsClient client;

    @Override
    public TwitchDataResponseDTO<TwitchChannelRewardDTO> getChannelRewards(@NonNull final String twitchAccessToken,
                                                                           @NonNull final String broadcasterId) {
        try {
            return client.getChannelRewards(
                    super.addBearerPrefix(twitchAccessToken),
                    broadcasterId
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
