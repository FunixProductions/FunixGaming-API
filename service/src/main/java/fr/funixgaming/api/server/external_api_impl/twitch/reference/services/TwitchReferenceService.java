package fr.funixgaming.api.server.external_api_impl.twitch.reference.services;

import feign.FeignException;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;

public abstract class TwitchReferenceService {

    protected String addBearerPrefix(final String accessToken) {
        if (accessToken.startsWith("Bearer ")) {
            return accessToken;
        } else {
            return "Bearer " + accessToken;
        }
    }

    protected ApiException handleFeignException(final FeignException e) {
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
