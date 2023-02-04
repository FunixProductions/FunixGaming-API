package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.entities;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "twitch_events_sub")
public class TwitchEventSub extends ApiEntity {

    @Column(name = "streamer_username", unique = true, updatable = false)
    private String streamerUsername;

}
