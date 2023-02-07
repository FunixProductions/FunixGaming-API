package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.repositories;

import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.entities.TwitchEventSubStreamer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TwitchEventSubStreamerRepository extends ApiRepository<TwitchEventSubStreamer> {

    Optional<TwitchEventSubStreamer> findTwitchEventSubStreamerByStreamerId(String streamerId);
    void deleteTwitchEventSubStreamersByStreamerId(String streamerId);

}
