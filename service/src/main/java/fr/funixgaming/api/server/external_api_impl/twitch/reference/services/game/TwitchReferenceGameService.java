package fr.funixgaming.api.server.external_api_impl.twitch.reference.services.game;

import feign.FeignException;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.game.TwitchGameDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.configs.TwitchReferenceService;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.game.TwitchReferenceGameClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TwitchReferenceGameService extends TwitchReferenceService implements TwitchReferenceGameClient {

    private final TwitchReferenceGameClient client;

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameInfoByName(String twitchAccessToken, String name) {
        try {
            return client.getGameInfoByName(super.addBearerPrefix(twitchAccessToken), name);
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchGameDTO> getGameInfoById(String twitchAccessToken, String id) {
        try {
            return client.getGameInfoById(super.addBearerPrefix(twitchAccessToken), id);
        } catch (FeignException e) {
            throw super.handleFeignException(e);
        }
    }
}
