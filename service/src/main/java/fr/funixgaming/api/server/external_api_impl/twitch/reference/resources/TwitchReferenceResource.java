package fr.funixgaming.api.server.external_api_impl.twitch.reference.resources;

import fr.funixgaming.api.client.external_api_impl.twitch.auth.dtos.TwitchClientTokenDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.auth.enums.TwitchClientTokenType;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchClientTokenService;
import fr.funixgaming.api.server.user.services.CurrentUser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class TwitchReferenceResource {

    private final TwitchClientTokenService twitchClientTokenService;
    private final CurrentUser currentUser;

    protected TwitchClientTokenDTO getTwitchAuthByUserConnected(final TwitchClientTokenType tokenType) throws ApiException {
        final UserDTO userDTO = currentUser.getCurrentUser();
        if (userDTO == null) {
            throw new ApiBadRequestException("Vous n'êtes pas connecté.");
        }

        return twitchClientTokenService.fetchToken(userDTO.getId(), tokenType);
    }

}
