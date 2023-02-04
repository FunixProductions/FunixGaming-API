package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

/**
 * Service used to handle new notifications from callback service like a new follower or sub
 */
@Service
public class TwitchEventSubHandler {

    public void receiveNewNotification(final String notificationType, final JsonObject event) {

    }

}
