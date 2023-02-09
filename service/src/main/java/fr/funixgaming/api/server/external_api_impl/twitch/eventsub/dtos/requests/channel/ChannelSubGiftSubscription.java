package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelsubscriptiongift">Doc</a>
 */
public class ChannelSubGiftSubscription extends ChannelSubscription {
    public ChannelSubGiftSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.SUB_GIFT.getType(), ChannelEventType.SUB_GIFT.getVersion());
    }
}
