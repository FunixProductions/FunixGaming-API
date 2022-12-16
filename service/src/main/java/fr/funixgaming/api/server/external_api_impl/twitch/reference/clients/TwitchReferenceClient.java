package fr.funixgaming.api.server.external_api_impl.twitch.reference.clients;

import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.requests.TwitchChannelUpdateDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.TwitchChannelDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelRewardDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelUserDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.stream.TwitchStreamDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.video.TwitchChannelClipCreationDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.video.TwitchChannelClipDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.user.TwitchFollowDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.user.TwitchUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "TwitchReferenceClient",
        url = "${twitch.api.app-api-domain-url}",
        configuration = TwitchReferenceRequestInterceptor.class,
        path = "helix"
)
public interface TwitchReferenceClient {

    /**
     * <p>Gets information about one or more channels.</p>
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId The ID of the broadcaster whose channel you want to get. Can be multiple
     * @return Channel information
     */
    @GetMapping("channels")
    TwitchDataResponseDTO<TwitchChannelDTO> getChannelInformation(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                  @RequestParam(name = "broadcaster_id") List<String> broadcasterId);

    /**
     * Updates a channel’s properties.
     * Requires a user access token that includes the channel:manage:broadcast scope.
     * @param twitchAccessToken Bearer {accessToken}
     * @param channelUpdateDTO update infos
     */
    @PatchMapping("channels")
    void updateChannelInformation(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                  @RequestParam(name = "broadcaster_id") String broadcasterId,
                                  @RequestBody TwitchChannelUpdateDTO channelUpdateDTO);

    /**
     * Gets a list of custom rewards that the specified broadcaster created.
     * Requires a user access token that includes the channel:read:redemptions scope.
     * NOTE: A channel may offer a maximum of 50 rewards, which includes both enabled and disabled rewards.
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId The ID of the broadcaster whose custom rewards you want to get. This ID must match the user ID found in the OAuth token.
     * @param manageableRewards A Boolean value that determines whether the response contains only the custom rewards that the app may manage
     *                          (the app is identified by the ID in the Client-Id header).
     *                          Set to true to get only the custom rewards that the app may manage. The default is false.
     * @param id A list of IDs to filter the rewards by. To specify more than one ID, include this parameter for each reward you want to get.
     * @return reward information
     */
    @GetMapping("channel_points/custom_rewards")
    TwitchDataResponseDTO<TwitchChannelRewardDTO> getChannelRewards(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                    @RequestParam(name = "broadcaster_id") String broadcasterId,
                                                                    @RequestParam(name = "only_manageable_rewards", defaultValue = "false") boolean manageableRewards,
                                                                    @RequestParam(required = false) List<String> id);

    /**
     * Gets the list of users that are connected to the broadcaster’s chat session.
     * NOTE: There is a delay between when users join and leave a chat and when the list is updated accordingly.
     * To determine whether a user is a moderator or VIP, use the Get Moderators and Get VIPs endpoints.
     * You can check the roles of up to 100 users.
     * Requires a user access token that includes the moderator:read:chatters scope.
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId The ID of the broadcaster whose list of chatters you want to get.
     * @param moderatorId The ID of the broadcaster or one of the broadcaster’s moderators. This ID must match the user ID in the user access token.
     * @param maxChattersReturned The maximum number of items to return per page in the response.
     *                            The minimum page size is 1 item per page and the maximum is 1,000. The default is 100.
     * @param paginationCursor The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return Chatters list
     */
    @GetMapping("chat/chatters")
    TwitchDataResponseDTO<TwitchChannelChattersDTO> getChannelChatters(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                       @RequestParam(name = "broadcaster_id") String broadcasterId,
                                                                       @RequestParam(name = "moderator_id") String moderatorId,
                                                                       @RequestParam(name = "first", required = false, defaultValue = "100") Integer maxChattersReturned,
                                                                       @RequestParam(name = "after", required = false, defaultValue = "") String paginationCursor);

    /**
     * Sends an announcement to the broadcaster’s chat room.
     * Requires a user access token that includes the moderator:manage:announcements scope.
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId The ID of the broadcaster that owns the chat room to send the announcement to.
     * @param moderatorId The ID of a user who has permission to moderate the broadcaster’s chat room,
     *                    or the broadcaster’s ID if they’re sending the announcement.
     *                    This ID must match the user ID in the user access token.
     */
    @PostMapping("chat/announcements")
    void sendChatAnnouncement(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                              @RequestParam(name = "broadcaster_id") String broadcasterId,
                              @RequestParam(name = "moderator_id") String moderatorId);

    /**
     * Creates a clip from the broadcaster’s stream.
     * Requires a user access token that includes the clips:edit scope.
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId The ID of the broadcaster whose stream you want to create a clip from.
     * @return clip creation instance with url and id to edit
     */
    @PostMapping("clips")
    TwitchChannelClipCreationDTO createClip(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                            @RequestParam(name = "broadcaster_id") String broadcasterId);

    /**
     * Gets one or more video clips that were captured from streams. For information about clips, see How to use clips.
     * Requires an app access token or user access token.
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId An ID that identifies the broadcaster whose video clips you want to get.
     *                      Use this parameter to get clips that were captured from the broadcaster’s streams.
     * @param startedAt	The start date used to filter clips. The API returns only clips within the start and end date window.
     *                  Specify the date and time in RFC3339 format.
     * @param endedAt The end date used to filter clips. If not specified, the time window is the start date plus one week.
     *                Specify the date and time in RFC3339 format.
     * @param amountReturned The maximum number of clips to return per page in the response.
     *                       The minimum page size is 1 clip per page and the maximum is 100.The default is 20.
     * @param before The cursor used to get the previous page of results. The Pagination object in the response contains the cursor’s value.
     * @param after The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return clip list
     */
    @GetMapping("clips")
    TwitchDataResponseDTO<TwitchChannelClipDTO> getStreamerClips(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                 @RequestParam(name = "broadcaster_id") String broadcasterId,
                                                                 @RequestParam(name = "started_at", required = false, defaultValue = "") String startedAt,
                                                                 @RequestParam(name = "ended_at", required = false, defaultValue = "") String endedAt,
                                                                 @RequestParam(name = "first", required = false, defaultValue = "20") Integer amountReturned,
                                                                 @RequestParam(name = "before", required = false, defaultValue = "") String before,
                                                                 @RequestParam(name = "after", required = false, defaultValue = "") String after);

    /**
     * Gets one or more video clips that were captured from streams. For information about clips, see How to use clips.
     * Requires an app access token or user access token.
     * @param twitchAccessToken Bearer {accessToken}
     * @param id An ID that identifies the clip to get. To specify more than one ID, include this parameter for each clip you want to get. For example,
     *           id=foo&id=bar. You may specify a maximum of 100 IDs. The API ignores duplicate IDs and IDs that aren’t found.
     * @param startedAt	The start date used to filter clips. The API returns only clips within the start and end date window.
     *                  Specify the date and time in RFC3339 format.
     * @param endedAt The end date used to filter clips. If not specified, the time window is the start date plus one week.
     *                Specify the date and time in RFC3339 format.
     * @param amountReturned The maximum number of clips to return per page in the response.
     *                       The minimum page size is 1 clip per page and the maximum is 100.The default is 20.
     * @param before The cursor used to get the previous page of results. The Pagination object in the response contains the cursor’s value.
     * @param after The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return clip list
     */
    @GetMapping("clips")
    TwitchDataResponseDTO<TwitchChannelClipDTO> getClipsById(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                             @RequestParam(name = "started_at", required = false, defaultValue = "") String startedAt,
                                                             @RequestParam(name = "ended_at", required = false, defaultValue = "") String endedAt,
                                                             @RequestParam(name = "first", required = false, defaultValue = "20") String amountReturned,
                                                             @RequestParam(name = "before", required = false, defaultValue = "") String before,
                                                             @RequestParam(name = "after", required = false, defaultValue = "") String after,
                                                             @RequestParam(name = "id") List<String> id);

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
     *                The list is returned in the same order as you specified the IDs.
     * @return moderators list
     */
    @GetMapping("moderation/moderators")
    TwitchDataResponseDTO<TwitchChannelUserDTO> getChannelModerators(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                     @RequestParam(name = "broadcaster_id") String streamerId,
                                                                     @RequestParam(name = "first", required = false, defaultValue = "20") String maximumReturned,
                                                                     @RequestParam(name = "after", required = false, defaultValue = "20") String after,
                                                                     @RequestParam(name = "user_id", required = false, defaultValue = "") List<String> userIds);

    /**
     * Gets a list of the broadcaster’s VIPs.
     * Requires a user access token that includes the channel:read:vips scope.
     * If your app also adds and removes VIP status, you can use the channel:manage:vips scope instead.
     * @param twitchAccessToken Bearer {accessToken}
     * @param streamerId The ID of the broadcaster whose list of moderators you want to get. This ID must match the user ID in the access token.
     * @param maximumReturned The maximum number of items to return per page in the response.
     *                        The minimum page size is 1 item per page and the maximum is 100 items per page. The default is 20.
     * @param after The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @param userIds Filters the list for specific VIPs. To specify more than one user, include the user_id parameter for each user to get.
     *                For example, &user_id=1234&user_id=5678. The maximum number of IDs that you may specify is 100.
     *                Ignores the ID of those users in the list that aren’t VIPs.
     * @return vip list
     */
    @GetMapping("channels/vips")
    TwitchDataResponseDTO<TwitchChannelUserDTO> getChannelVips(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                               @RequestParam(name = "broadcaster_id") String streamerId,
                                                               @RequestParam(name = "first", required = false, defaultValue = "20") String maximumReturned,
                                                               @RequestParam(name = "after", required = false, defaultValue = "20") String after,
                                                               @RequestParam(name = "user_id", required = false, defaultValue = "") List<String> userIds);

    /**
     * Gets a list of all broadcasters that are streaming.
     * The list is in descending order by the number of viewers watching the stream.
     * Because viewers come and go during a stream,it’s possible to find duplicate or missing streams in the list as you page through the results.
     * @param twitchAccessToken Requires an app access token or user access token. Bearer {accessToken}
     * @param streamerName 	A user login name used to filter the list of streams. Returns only the streams of those users that are broadcasting.
     *                      You may specify a maximum of 100 login names. To specify multiple names, include the user_login parameter for each user.
     *                      For example, &user_login=foo&user_login=bar.
     * @param maximumReturned The maximum number of items to return per page in the response.
     *                        The minimum page size is 1 item per page and the maximum is 100 items per page. The default is 20.
     * @param cursorBefore The cursor used to get the previous page of results. The Pagination object in the response contains the cursor’s value.
     * @param cursorAfter The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return list streams
     */
    @GetMapping("streams")
    TwitchDataResponseDTO<TwitchStreamDTO> getStreams(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                      @RequestParam(name = "user_login") String streamerName,
                                                      @RequestParam(name = "first", required = false, defaultValue = "20") String maximumReturned,
                                                      @RequestParam(name = "before", required = false, defaultValue = "") String cursorBefore,
                                                      @RequestParam(name = "after", required = false, defaultValue = "") String cursorAfter);

    /**
     * Gets information about one or more users.
     * To include the user’s verified email address in the response, you must use a user access token that includes the user:read:email scope.
     * @param twitchAccessToken Requires an app access token or user access token.
     * @param name name list to get
     * @return users
     */
    @GetMapping("users")
    TwitchDataResponseDTO<TwitchUserDTO> getUsersByName(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                        @RequestParam(name = "login") List<String> name);

    /**
     * Gets information about one or more users.
     * To include the user’s verified email address in the response, you must use a user access token that includes the user:read:email scope.
     * @param twitchAccessToken Requires an app access token or user access token.
     * @param id id list to get
     * @return users
     */
    @GetMapping("users")
    TwitchDataResponseDTO<TwitchUserDTO> getUsersById(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                      @RequestParam(name = "id") List<String> id);

    /**
     * Fetch the follow list of a user
     * @param twitchAccessToken Requires an app access token or user access token: Bearer token
     * @param userId user id to fetch
     * @param maximumReturned The maximum number of items to return per page
     * @param cursorAfter The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return follow list
     */
    @GetMapping("users/follows")
    TwitchDataResponseDTO<TwitchFollowDTO> getUserFollowingList(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                @RequestParam(name = "from_id", required = false, defaultValue = "") String userId,
                                                                @RequestParam(name = "first", required = false, defaultValue = "20") String maximumReturned,
                                                                @RequestParam(name = "after", required = false, defaultValue = "") String cursorAfter);

    /**
     * Fetch the followers from a user
     * @param twitchAccessToken Requires an app access token or user access token: Bearer token
     * @param userId user id to fetch
     * @param maximumReturned The maximum number of items to return per page
     * @param cursorAfter The cursor used to get the next page of results. The Pagination object in the response contains the cursor’s value.
     * @return followers list
     */
    @GetMapping("users/follows")
    TwitchDataResponseDTO<TwitchFollowDTO> getUserFollowersList(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                @RequestParam(name = "to_id", required = false, defaultValue = "") String userId,
                                                                @RequestParam(name = "first", required = false, defaultValue = "20") String maximumReturned,
                                                                @RequestParam(name = "after", required = false, defaultValue = "") String cursorAfter);

    /**
     * Check if a user is following a streamer
     * @param twitchAccessToken Requires an app access token or user access token: Bearer token
     * @param userId viewer id to check
     * @param streamerId streamer id to check
     * @return a single element list if the user is following otherwise not following
     */
    @GetMapping("users/follows")
    TwitchDataResponseDTO<TwitchFollowDTO> isUserFollowingStreamer(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                   @RequestParam(name = "from_id", required = false, defaultValue = "") String userId,
                                                                   @RequestParam(name = "to_id", required = false, defaultValue = "") String streamerId;

}
