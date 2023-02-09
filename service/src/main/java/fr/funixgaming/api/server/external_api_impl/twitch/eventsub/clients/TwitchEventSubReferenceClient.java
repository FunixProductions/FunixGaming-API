package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.clients;

import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.TwitchEventSubListDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.configs.TwitchReferenceRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "TwitchEventSubReferenceClient",
        url = "${twitch.api.app-api-domain-url}",
        path = "/helix/eventsub/subscriptions",
        configuration = TwitchReferenceRequestInterceptor.class
)
public interface TwitchEventSubReferenceClient {

    /**
     * Get subscriptions
     * <a href="https://dev.twitch.tv/docs/api/reference/#get-eventsub-subscriptions">Doc</a>
     * @param twitchAccessToken app access token
     * @param status Filter subscriptions by its status. Possible values are:
     *               enabled — The subscription is enabled.
     *               notification_failures_exceeded — The notification delivery failure rate was too high.
     *               authorization_revoked — The authorization was revoked for one or more users specified in the Condition object.
     *               moderator_removed — The moderator that authorized the subscription is no longer one of the broadcaster's moderators.
     *               user_removed — One of the users specified in the Condition object was removed.
     * @param type Filter subscriptions by subscription type. For a list of subscription types, see <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types#subscription-types">Subscription Types</a>.
     * @param userId Filter subscriptions by user ID. The response contains subscriptions where this ID matches a user ID that you specified in the Condition object when you created the subscription.
     * @param after The cursor used to get the next page of results. The pagination object in the response contains the cursor’s value.
     * @return sub list
     */
    @GetMapping
    TwitchEventSubListDTO getSubscriptions(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "user_id", required = false) String userId,
            @RequestParam(name = "after", required = false) String after
    );

    /**
     * Create subscription
     * <a href="https://dev.twitch.tv/docs/api/reference/#create-eventsub-subscription">Doc</a>
     * @param twitchAccessToken app access token
     * @param request request
     */
    @PostMapping
    void createSubscription(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
            @RequestBody String request
    );

    /**
     * Remove a subscription
     * <a href="https://dev.twitch.tv/docs/api/reference/#delete-eventsub-subscription">Doc</a>
     * @param twitchAccessToken app access token
     * @param subscriptionId sub id
     */
    @DeleteMapping
    void deleteSubscription(
            @RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
            @RequestParam(name = "id") String subscriptionId
    );

}