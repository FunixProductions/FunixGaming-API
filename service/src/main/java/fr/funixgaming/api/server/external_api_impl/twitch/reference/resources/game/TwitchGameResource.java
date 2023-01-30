package fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.game;

import fr.funixgaming.api.client.external_api_impl.twitch.auth.dtos.TwitchClientTokenDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.clients.game.TwitchGameClient;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.game.TwitchGameDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchClientTokenService;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.TwitchReferenceResource;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.game.TwitchReferenceGameService;
import fr.funixgaming.api.server.user.services.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/game")
public class TwitchGameResource extends TwitchReferenceResource implements TwitchGameClient {

    private final TwitchReferenceGameService gameService;

    public TwitchGameResource(TwitchClientTokenService tokenService,
                              CurrentSession currentSession,
                              TwitchReferenceGameService gameService) {
        super(tokenService, currentSession);
        this.gameService = gameService;
    }

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameByName(String name) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return gameService.getGameInfoByName(tokenDTO.getAccessToken(), name);
    }

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameById(String id) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return gameService.getGameInfoById(tokenDTO.getAccessToken(), id);
    }
}
