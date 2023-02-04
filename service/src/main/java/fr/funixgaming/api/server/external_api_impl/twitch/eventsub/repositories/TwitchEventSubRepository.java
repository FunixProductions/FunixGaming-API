package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.repositories;

import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.entities.TwitchEventSub;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwitchEventSubRepository extends ApiRepository<TwitchEventSub> {

    Optional<TwitchEventSub> findTwitchEventSubByStreamerUsernameEqualsIgnoreCase(String streamerName);

}
