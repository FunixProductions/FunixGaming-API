package fr.funixgaming.api.twitch.service.ressources;

import com.funixproductions.api.twitch.reference.client.clients.game.TwitchGameClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.game.TwitchGameDTO;
import com.funixproductions.core.exceptions.ApiException;
import fr.funixgaming.api.twitch.client.clients.FunixGamingTwitchGameClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/games")
@RequiredArgsConstructor
public class TwitchGameResource implements FunixGamingTwitchGameClient {

    private final TwitchGameClient twitchGameClient;

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameByName(String name) {
        try {
            return twitchGameClient.getGameByName(name);
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Erreur interne: " + e.getMessage(), e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameById(String id) {
        try {
            return twitchGameClient.getGameById(id);
        }  catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException("Erreur interne: " + e.getMessage(), e);
        }
    }
}
