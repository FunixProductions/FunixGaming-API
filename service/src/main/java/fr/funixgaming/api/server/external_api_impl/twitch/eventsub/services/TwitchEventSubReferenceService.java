package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services;

import feign.FeignException;
import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.TwitchEventSubListDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchServerTokenService;
import fr.funixgaming.api.server.external_api_impl.twitch.configs.TwitchApiConfig;
import fr.funixgaming.api.server.external_api_impl.twitch.configs.TwitchReferenceService;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.clients.TwitchEventSubReferenceClient;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.TwitchSubscription;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * Service who encapsulates the twitch client who calls the twitch api
 */
@Service
@RequiredArgsConstructor
public class TwitchEventSubReferenceService extends TwitchReferenceService {

    private final TwitchApiConfig twitchApiConfig;
    private final TwitchEventSubHmacService hmacService;
    private final TwitchServerTokenService tokenService;
    private final TwitchEventSubReferenceClient eventSubReferenceClient;

    public TwitchEventSubListDTO getSubscriptions(@Nullable String status,
                                                  @Nullable String type,
                                                  @Nullable String userId,
                                                  @Nullable String after) {
        try {
            return this.eventSubReferenceClient.getSubscriptions(
                    super.addBearerPrefix(tokenService.getAccessToken()),
                    status,
                    type,
                    userId,
                    after
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    public void createSubscription(@NonNull final TwitchSubscription request) {
        request.setEventUrlCallback(this.twitchApiConfig.getAppEventSubCallback());
        request.setSecretHmacKey(hmacService.getKey());

        try {
            this.eventSubReferenceClient.createSubscription(
                    super.addBearerPrefix(tokenService.getAccessToken()),
                    MediaType.APPLICATION_JSON_VALUE,
                    request.getPayload());
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    public void deleteSubscription(@NonNull String subscriptionId) {
        try {
            this.eventSubReferenceClient.deleteSubscription(
                    super.addBearerPrefix(this.tokenService.getAccessToken()),
                    subscriptionId
            );
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
