package fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.websocket;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchEventSubWebsocketMessage {

    private String streamerId;

    private String notificationType;

    private String event;

}
