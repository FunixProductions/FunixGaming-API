package fr.funixgaming.api.server.external_api_impl.twitch.reference.services;

import com.google.common.base.Strings;
import feign.FeignException;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.TwitchReferenceClient;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.requests.TwitchChannelUpdateDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.TwitchChannelDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelRewardDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TwitchReferenceService implements TwitchReferenceClient {

    private final TwitchReferenceClient client;

    @Override
    public TwitchDataResponseDTO<TwitchChannelDTO> getChannelInformation(String twitchAccessToken, String... broadcasterId) {
        try {
            return client.getChannelInformation(addBearerPrefix(twitchAccessToken), broadcasterId);
        } catch (FeignException e) {
            throw handleFeignException(e);
        }
    }

    @Override
    public void updateChannelInformation(String twitchAccessToken, String broadcasterId, TwitchChannelUpdateDTO channelUpdateDTO) {
        try {
            client.updateChannelInformation(addBearerPrefix(twitchAccessToken), broadcasterId, channelUpdateDTO);
        } catch (FeignException e) {
            throw handleFeignException(e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelRewardDTO> getChannelRewards(String twitchAccessToken,
                                                                           String broadcasterId,
                                                                           boolean manageableRewards,
                                                                           String... id) {
        try {
            return client.getChannelRewards(twitchAccessToken, broadcasterId, manageableRewards, id);
        } catch (FeignException e) {
            throw handleFeignException(e);
        }
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelChattersDTO> getChannelChatters(String twitchAccessToken,
                                                                              String broadcasterId,
                                                                              String moderatorId,
                                                                              @Nullable Integer maxChattersReturned,
                                                                              @Nullable String paginationCursor) {
        if (Strings.isNullOrEmpty(paginationCursor)) {
            paginationCursor = "";
        }
        if (maxChattersReturned == null || maxChattersReturned < 1) {
            maxChattersReturned = 100;
        }

        try {
            return client.getChannelChatters(twitchAccessToken, broadcasterId, moderatorId, maxChattersReturned, paginationCursor);
        } catch (FeignException e) {
            throw handleFeignException(e);
        }
    }

    private String addBearerPrefix(final String accessToken) {
        if (accessToken.startsWith("Bearer ")) {
            return accessToken;
        } else {
            return "Bearer " + accessToken;
        }
    }

    private ApiException handleFeignException(final FeignException e) {
        final int code = e.status();
        final String errorMessage = e.getMessage();

        switch (code) {
            case 400 -> throw new ApiBadRequestException(String.format("Erreur lors de la requête Twitch: %s.", errorMessage), e);
            case 401 -> throw new ApiBadRequestException(String.format("Votre token d'accès Twitch est invalide. Erreur: %s", errorMessage), e);
            case 403 -> throw new ApiBadRequestException(String.format("Accès refusé à la ressource Twitch. Erreur: %s", errorMessage), e);
            case 404 -> throw new ApiBadRequestException(String.format("Ressouce Twitch introuvable. Erreur: %s", errorMessage), e);
            case 429 -> throw new ApiBadRequestException("Vous faites trop de requêtes à Twitch.", e);
            default -> throw new ApiBadRequestException(String.format("Erreur fatale twitch: %s", errorMessage), e);
        }
    }
}
