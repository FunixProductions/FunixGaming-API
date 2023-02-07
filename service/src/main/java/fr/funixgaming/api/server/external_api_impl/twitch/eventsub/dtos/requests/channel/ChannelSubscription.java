package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import com.google.gson.JsonObject;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.TwitchSubscription;

public abstract class ChannelSubscription extends TwitchSubscription {

    private final String streamerId;

    public ChannelSubscription(String streamerId, String type, String version) {
        super(type, version);
        this.streamerId = streamerId;
    }

    @Override
    protected JsonObject getCondition() {
        final JsonObject condition = new JsonObject();

        condition.addProperty("broadcaster_user_id", this.streamerId);
        return condition;
    }

    public final String getStreamerId() {
        return streamerId;
    }
}
