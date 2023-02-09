package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import com.google.gson.JsonObject;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelShoutOutReceiveSubscription extends ChannelSubscription {

    public ChannelShoutOutReceiveSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.SHOUTOUT_RECEIVE.getType(), ChannelEventType.SHOUTOUT_RECEIVE.getVersion());
    }

    @Override
    protected JsonObject getCondition() {
        final JsonObject condition = new JsonObject();

        condition.addProperty("broadcaster_user_id", getStreamerId());
        condition.addProperty("moderator_user_id", getStreamerId());
        return condition;
    }
}
