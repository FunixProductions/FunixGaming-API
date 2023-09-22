package fr.funixgaming.api.twitch.service.ressources;

import com.funixproductions.api.twitch.reference.client.clients.channel.TwitchChannelClient;
import com.funixproductions.api.twitch.reference.client.clients.users.TwitchUsersClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchFollowDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.user.TwitchUserDTO;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.core.exceptions.ApiException;
import fr.funixgaming.api.twitch.client.clients.FunixGamingTwitchUserClient;
import fr.funixgaming.api.twitch.service.services.FunixGamingInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/twitch/user")
@RequiredArgsConstructor
public class TwitchUserResource implements FunixGamingTwitchUserClient {

    private final TwitchUsersClient usersClient;
    private final TwitchChannelClient channelClient;
    private final FunixGamingInformationService funixGamingInformationService;

    @Override
    public TwitchDataResponseDTO<TwitchFollowDTO> isUserFollowingStreamer(String userId) {
        try {
            final UserDTO userDTO = funixGamingInformationService.getFunixGamingUser();

            return channelClient.getChannelFollowers(null, null, userId, userDTO.getId().toString());
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Erreur interne: " + e.getMessage(), e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchUserDTO> getUsersByName(List<String> name) {
        try {
            return usersClient.getUsersByName(name);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Erreur interne: " + e.getMessage(), e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchUserDTO> getUsersById(List<String> id) {
        try {
            return usersClient.getUsersById(id);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Erreur interne: " + e.getMessage(), e);
        }
    }
}
