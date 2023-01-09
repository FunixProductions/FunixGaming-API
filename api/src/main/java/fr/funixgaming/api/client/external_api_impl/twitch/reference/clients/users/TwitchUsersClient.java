package fr.funixgaming.api.client.external_api_impl.twitch.reference.clients.users;

import fr.funixgaming.api.client.config.FeignConfig;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.user.TwitchFollowDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchUsersClient",
        url = "${funix.api.app-domain-url}",
        path = "twitch/users",
        configuration = FeignConfig.class
)
public interface TwitchUsersClient {

    /**
     * Check if a user is following a streamer
     * @param userId viewer id to check
     * @param streamerId streamer id to check
     * @return a single element list if the user is following otherwise not following
     */
    @GetMapping("is_following")
    TwitchDataResponseDTO<TwitchFollowDTO> isUserFollowingStreamer(@RequestParam(name = "user_id") String userId,
                                                                   @RequestParam(name = "streamer_id") String streamerId);

}
