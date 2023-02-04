package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelfollow">Documentation</a>
 */
public class ChannelFollowSubscription extends ChannelSubscription {

    public ChannelFollowSubscription(String streamerId) {
        super(streamerId, ChannelEventType.FOLLOW.getType(), ChannelEventType.FOLLOW.getVersion());
    }

}
