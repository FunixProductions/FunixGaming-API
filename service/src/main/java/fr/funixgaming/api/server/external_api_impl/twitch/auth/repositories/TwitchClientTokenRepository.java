package fr.funixgaming.api.server.external_api_impl.twitch.auth.repositories;

import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.entities.TwitchClientToken;
import fr.funixgaming.api.server.user.entities.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwitchClientTokenRepository extends ApiRepository<TwitchClientToken> {
    Optional<TwitchClientToken> findTwitchClientTokenByUser(User user);
    Optional<TwitchClientToken> findTwitchClientTokenByTwitchUsernameEqualsIgnoreCase(String username);
}
