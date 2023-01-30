package fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.users;

import fr.funixgaming.api.client.external_api_impl.twitch.auth.dtos.TwitchClientTokenDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.clients.users.TwitchUsersClient;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.user.TwitchFollowDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchClientTokenService;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.TwitchReferenceResource;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.users.TwitchReferenceUsersService;
import fr.funixgaming.api.server.user.services.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/users")
public class TwitchUsersResource extends TwitchReferenceResource implements TwitchUsersClient {

    private final TwitchReferenceUsersService service;

    public TwitchUsersResource(CurrentSession currentSession,
                               TwitchClientTokenService tokenService,
                               TwitchReferenceUsersService service) {
        super(tokenService, currentSession);
        this.service = service;
    }

    @Override
    public TwitchDataResponseDTO<TwitchFollowDTO> isUserFollowingStreamer(String userId, String streamerId) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return service.isUserFollowingStreamer(
                tokenDTO.getAccessToken(),
                userId,
                streamerId
        );
    }
}
