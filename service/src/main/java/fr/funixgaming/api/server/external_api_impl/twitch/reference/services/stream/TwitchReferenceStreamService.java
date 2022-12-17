package fr.funixgaming.api.server.external_api_impl.twitch.reference.services.stream;

import feign.FeignException;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.stream.TwitchReferenceStreamsClient;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.stream.TwitchStreamDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.TwitchReferenceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwitchReferenceStreamService extends TwitchReferenceService implements TwitchReferenceStreamsClient {

    private final TwitchReferenceStreamsClient client;

    @Override
    public TwitchDataResponseDTO<TwitchStreamDTO> getStreams(@NonNull final String twitchAccessToken,
                                                             @NonNull final String streamerName) {
        try {
            return client.getStreams(
                    super.addBearerPrefix(twitchAccessToken),
                    streamerName
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
