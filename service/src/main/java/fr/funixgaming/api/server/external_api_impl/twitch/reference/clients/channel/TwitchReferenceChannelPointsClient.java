package fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.TwitchReferenceRequestInterceptor;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelRewardDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <a href="https://dev.twitch.tv/docs/api/reference#create-custom-rewards">Doc twitch</a>
 */
@FeignClient(
        name = "TwitchReferenceClient",
        url = "${twitch.api.app-api-domain-url}",
        configuration = TwitchReferenceRequestInterceptor.class,
        path = "helix/channel_points/custom_rewards"
)
public interface TwitchReferenceChannelPointsClient {

    /**
     * Gets a list of custom rewards that the specified broadcaster created.
     * Requires a user access token that includes the channel:read:redemptions scope.
     * NOTE: A channel may offer a maximum of 50 rewards, which includes both enabled and disabled rewards.
     * @param twitchAccessToken Requires a user access token that includes the channel:read:redemptions scope.
     * @param broadcasterId The ID of the broadcaster whose custom rewards you want to get. This ID must match the user ID found in the OAuth token.
     * @return reward information
     */
    @GetMapping
    TwitchDataResponseDTO<TwitchChannelRewardDTO> getChannelRewards(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                    @RequestParam(name = "broadcaster_id") String broadcasterId);


}
