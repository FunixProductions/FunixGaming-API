package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import com.google.gson.JsonObject;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelfollow">Documentation</a>
 */
public class ChannelFollowSubscription extends ChannelSubscription {

    public ChannelFollowSubscription(String streamerId) {
        super(streamerId, ChannelEventType.FOLLOW.getType(), ChannelEventType.FOLLOW.getVersion());
    }

    @Override
    protected JsonObject getCondition() {
        final JsonObject condition = new JsonObject();

        condition.addProperty("broadcaster_user_id", super.getStreamerId());
        condition.addProperty("moderator_user_id", super.getStreamerId());
        return condition;
    }
}
