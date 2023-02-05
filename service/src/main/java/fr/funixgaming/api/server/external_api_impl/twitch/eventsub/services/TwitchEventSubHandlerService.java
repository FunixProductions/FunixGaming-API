package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.events.channel.TwitchEventChannelFollowDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.events.channel.TwitchEventChannelUpdateDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;
import org.springframework.stereotype.Service;

/**
 * Service used to handle new notifications from callback service like a new follower or sub
 */
@Service
public class TwitchEventSubHandlerService {

    private final TwitchEventSubWebsocketService websocketService;
    private final Gson gson;

    public TwitchEventSubHandlerService(TwitchEventSubWebsocketService websocketService) {
        this.websocketService = websocketService;
        this.gson = new Gson();
    }

    public void receiveNewNotification(final String notificationType, final JsonObject event) {
        websocketService.newNotification(notificationType, event.toString());

        if (notificationType.startsWith("channel")) {
            handleChannelNotification(notificationType, event);
        }
    }

    private void handleChannelNotification(final String notificationType, final JsonObject event) {
        if (notificationType.equals(ChannelEventType.FOLLOW.getType())) {
            final TwitchEventChannelFollowDTO followDTO = gson.fromJson(event, TwitchEventChannelFollowDTO.class);

        } else if (notificationType.equals(ChannelEventType.UPDATE.getType())) {
            final TwitchEventChannelUpdateDTO updateDTO = gson.fromJson(event, TwitchEventChannelUpdateDTO.class);
        }
    }

}
