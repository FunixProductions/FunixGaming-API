package fr.funixgaming.api.server.external_api_impl.twitch.reference.services;

import feign.FeignException;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.TwitchReferenceClient;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.TwitchChannelDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TwitchReferenceService implements TwitchReferenceClient {

    private final TwitchReferenceClient client;

    @Override
    public List<TwitchChannelDTO> getChannelInformation(String twitchAccessToken, String... broadcasterId) {
        try {
            return client.getChannelInformation(twitchAccessToken, broadcasterId);
        } catch (FeignException e) {
            throw handleFeignException(e);
        }
    }

    private ApiException handleFeignException(final FeignException e) {
        final int code = e.status();
        final String errorMessage = e.getMessage();

        switch (code) {
            case 400 -> throw new ApiBadRequestException(String.format("Erreur lors de la requête twitch: %s.", errorMessage), e);
            case 401 -> throw new ApiBadRequestException("Votre token d'accès twitch est invalide.", e);
            case 429 -> throw new ApiBadRequestException("Vous faites trop de requêtes à twitch.", e);
            default -> throw new ApiBadRequestException(String.format("Erreur fatale twitch: %s", errorMessage), e);
        }
    }
}
