package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelupdate">Documentation</a>
 */
public class ChannelUpdateSubscription extends ChannelSubscription {

    public ChannelUpdateSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.UPDATE.getType(), ChannelEventType.UPDATE.getVersion());
    }

}
