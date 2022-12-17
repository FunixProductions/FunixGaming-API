package fr.funixgaming.api.server.external_api_impl.twitch.reference.services.chat;

import com.google.common.base.Strings;
import feign.FeignException;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.chat.TwitchReferenceModerationClient;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelUserDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.TwitchReferenceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TwitchReferenceModerationService extends TwitchReferenceService implements TwitchReferenceModerationClient {

    private final TwitchReferenceModerationClient client;

    @Override
    public TwitchDataResponseDTO<TwitchChannelUserDTO> getChannelModerators(@NonNull final String twitchAccessToken,
                                                                            @NonNull final String streamerId,
                                                                            @Nullable final String maximumReturned,
                                                                            @Nullable final String after,
                                                                            @Nullable final List<String> userIds) {
        try {
            return client.getChannelModerators(
                    super.addBearerPrefix(twitchAccessToken),
                    streamerId,
                    Strings.isNullOrEmpty(maximumReturned) ? "20" : maximumReturned,
                    Strings.isNullOrEmpty(after) ? null : after,
                    userIds
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
