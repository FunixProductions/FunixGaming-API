package fr.funixgaming.api.twitch.service.ressources;

import com.funixproductions.api.twitch.reference.client.clients.users.TwitchUsersClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchFollowDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchUserDTO;
import fr.funixgaming.api.twitch.client.clients.FunixGamingTwitchUserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/twitch/user")
@RequiredArgsConstructor
public class TwitchUserResource implements FunixGamingTwitchUserClient {

    private final TwitchUsersClient service;

    @Override
    public TwitchDataResponseDTO<TwitchFollowDTO> isUserFollowingStreamer(String userId, String streamerId) {
        return service.isUserFollowingStreamer(userId, streamerId);
    }

    @Override
    public TwitchDataResponseDTO<TwitchUserDTO> getUsersByName(List<String> name) {
        return service.getUsersByName(name);
    }

    @Override
    public TwitchDataResponseDTO<TwitchUserDTO> getUsersById(List<String> id) {
        return service.getUsersById(id);
    }
}
