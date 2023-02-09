package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services.handler;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.events.channel.TwitchEventChannelFollowDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.events.channel.TwitchEventChannelUpdateDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services.websocket.TwitchEventSubWebsocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Service used to handle new notifications from callback service like a new follower or sub
 */
@Service
@RequiredArgsConstructor
public class TwitchEventSubHandlerService {

    private final TwitchEventSubWebsocketService websocketService;
    private final Gson gson = new Gson();

    @Async
    public void receiveNewNotification(final String notificationType, final JsonObject event) {
        final String streamerId = getStreamerIdInNotification(event);
        websocketService.newNotification(notificationType, streamerId, event.toString());

        if (notificationType.startsWith("channel") && !Strings.isNullOrEmpty(streamerId)) {
            handleChannelNotification(notificationType, streamerId, event);
        }
    }

    private void handleChannelNotification(final String notificationType, final String streamerId, final JsonObject event) {
        if (notificationType.equals(ChannelEventType.FOLLOW.getType())) {
            final TwitchEventChannelFollowDTO followDTO = gson.fromJson(event, TwitchEventChannelFollowDTO.class);

        } else if (notificationType.equals(ChannelEventType.UPDATE.getType())) {
            final TwitchEventChannelUpdateDTO updateDTO = gson.fromJson(event, TwitchEventChannelUpdateDTO.class);
        }
    }

    @Nullable
    private String getStreamerIdInNotification(final JsonObject jsonObject) {
        final JsonElement idJson = jsonObject.get("broadcaster_user_id");

        if (idJson.isJsonPrimitive()) {
            final JsonPrimitive id = idJson.getAsJsonPrimitive();
            return id.getAsString();
        } else {
            return null;
        }
    }

}
