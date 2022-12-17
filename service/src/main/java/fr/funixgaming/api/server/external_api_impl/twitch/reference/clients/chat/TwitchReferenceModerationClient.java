package fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.chat;

import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelUserDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.TwitchReferenceRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "TwitchReferenceModerationClient",
        url = "${twitch.api.app-api-domain-url}",
        configuration = TwitchReferenceRequestInterceptor.class,
        path = "helix/moderation"
)
public interface TwitchReferenceModerationClient {

    /**
     * Gets all users allowed to moderate the broadcaster’s chat room.
     * Requires a user access token that includes the moderation:read scope.
     * If your app also adds and removes moderators, you can use the channel:manage:moderators scope instead.
     * @param twitchAccessToken Bearer {accessToken}
     * @param streamerId The ID of the broadcaster whose list of moderators you want to get. This ID must match the user ID in the access token.
     * @param maximumReturned The maximum number of items to return per page in the response.
     *                        The minimum page size is 1 item per page and the maximum is 100 items per page. The default is 20.
     * @param after The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @param userIds A list of user IDs used to filter the results. To specify more than one ID, include this parameter for each moderator you want to get.
     *                For example, user_id=1234&user_id=5678. You may specify a maximum of 100 IDs.
     *                The returned list includes only the users from the list who are moderators in the broadcaster’s channel.
     *                The list is in the same order as you specified the IDs.
     * @return moderators list
     */
    @GetMapping("moderators")
    TwitchDataResponseDTO<TwitchChannelUserDTO> getChannelModerators(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                     @RequestParam(name = "broadcaster_id") String streamerId,
                                                                     @RequestParam(name = "first", required = false, defaultValue = "20") String maximumReturned,
                                                                     @RequestParam(name = "after", required = false, defaultValue = "20") String after,
                                                                     @RequestParam(name = "user_id", required = false, defaultValue = "") List<String> userIds);

}
