package fr.funixgaming.api.server.external_api_impl.twitch.repositories;

import fr.funixgaming.api.client.external_api_impl.twitch.enums.TwitchClientTokenType;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import fr.funixgaming.api.server.external_api_impl.twitch.entities.TwitchClientToken;
import fr.funixgaming.api.server.user.entities.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwitchClientTokenRepository extends ApiRepository<TwitchClientToken> {
    Optional<TwitchClientToken> findTwitchClientTokenByUserAndTokenType(User user, TwitchClientTokenType tokenType);
    void deleteTwitchClientTokenByoAuthCode(String oAuthCode);
}
