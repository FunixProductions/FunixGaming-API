package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import com.google.gson.JsonObject;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelRaidReceivedSubscription extends ChannelSubscription {
    public ChannelRaidReceivedSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.RAID_RECEIVED.getType(), ChannelEventType.RAID_RECEIVED.getVersion());
    }

    @Override
    protected JsonObject getCondition() {
        final JsonObject condition = new JsonObject();

        condition.addProperty("to_broadcaster_user_id", getStreamerId());
        return condition;
    }
}
