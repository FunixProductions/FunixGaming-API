package fr.funixgaming.api.twitch.client.clients;

import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchFollowDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchUserDTO;
import fr.funixgaming.api.core.client.clients.FeignTokenInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "FunixGamingTwitchUserClient",
        url = "${funixgaming.api.twitch.app-domain-url}",
        path = "/twitch/user",
        configuration = FeignTokenInterceptor.class
)
public interface FunixGamingTwitchUserClient {

    /**
     * Check if a user is following a streamer
     * @param userId viewer id to check
     * @return a single element list if the user is following otherwise not following
     */
    @GetMapping("isFollowing")
    TwitchDataResponseDTO<TwitchFollowDTO> isUserFollowingStreamer(@RequestParam(name = "user_id") String userId);

    @GetMapping("usersByName")
    TwitchDataResponseDTO<TwitchUserDTO> getUsersByName(@RequestParam(name = "login") List<String> name);

    @GetMapping("usersById")
    TwitchDataResponseDTO<TwitchUserDTO> getUsersById(@RequestParam(name = "id") List<String> id);

}
