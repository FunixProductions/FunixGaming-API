package fr.funixgaming.api.twitch.client.clients;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchFollowDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "TwitchStreamClient",
        url = "${funixgaming.api.twitch.app-domain-url}",
        path = "/twitch/user"
)
public interface FunixGamingTwitchUserClient {

    /**
     * Check if a user is following a streamer
     * @param userId viewer id to check
     * @param streamerId streamer id to check
     * @return a single element list if the user is following otherwise not following
     */
    @GetMapping("is_following")
    TwitchDataResponseDTO<TwitchFollowDTO> isUserFollowingStreamer(@RequestParam(name = "user_id") String userId,
                                                                   @RequestParam(name = "streamer_id") String streamerId);

    @GetMapping("usersByName")
    TwitchDataResponseDTO<TwitchUserDTO> getUsersByName(@RequestParam(name = "login") List<String> name);

    @GetMapping("usersById")
    TwitchDataResponseDTO<TwitchUserDTO> getUsersById(@RequestParam(name = "id") List<String> id);

}
