package fr.funixgaming.api.server.external_api_impl.twitch.auth.resources;

import com.google.common.base.Strings;
import fr.funixgaming.api.client.external_api_impl.twitch.auth.clients.TwitchAuthClient;
import fr.funixgaming.api.client.external_api_impl.twitch.auth.dtos.TwitchClientTokenDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.auth.enums.TwitchClientTokenType;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchClientTokenService;
import fr.funixgaming.api.server.user.services.CurrentSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/auth")
@RequiredArgsConstructor
public class TwitchAuthResource implements TwitchAuthClient {

    private final TwitchClientTokenService twitchClientTokenService;
    private final CurrentSession currentSession;

    @Override
    public String getAuthClientUrl(String tokenType) {
        return this.twitchClientTokenService.getAuthClientUrl(getTokenTypeByString(tokenType));
    }

    @Override
    public TwitchClientTokenDTO getAccessToken() {
        final UserDTO actualUser = currentSession.getCurrentUser();

        if (actualUser == null) {
            throw new ApiBadRequestException("Vous n'êtes pas connecté à l'api.");
        } else {
            return this.twitchClientTokenService.fetchToken(actualUser.getId());
        }
    }

    @GetMapping("cb")
    public String authClientCallback(@RequestParam(required = false) String code,
                                     @RequestParam(required = false) String state,
                                     @RequestParam(required = false) String error,
                                     @RequestParam(required = false, name = "error_description") String errorMessage) {
        if (!Strings.isNullOrEmpty(error) || !Strings.isNullOrEmpty(errorMessage)) {
            return "Une erreur Twitch est survenue. Veuillez vérifier que vous avez autorisé l'application FunixAPI sur Twitch.";
        } else {
            this.twitchClientTokenService.registerNewAuthorizationAuthToken(code, state);
            return "Votre compte est connecté avec Twitch ! Vous pouvez fermer cette fenêtre.";
        }
    }

    private TwitchClientTokenType getTokenTypeByString(final String tokenType) {
        for (final TwitchClientTokenType token : TwitchClientTokenType.values()) {
            if (token.name().equalsIgnoreCase(tokenType)) {
                return token;
            }
        }

        return TwitchClientTokenType.VIEWER;
    }
}
