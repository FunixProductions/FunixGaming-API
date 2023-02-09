package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

/**
 * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelsubscriptionmessage">Doc</a>
 */
public class ChannelResubMessageSubscription extends ChannelSubscription {

    public ChannelResubMessageSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.RESUB_MESSAGE.getType(), ChannelEventType.RESUB_MESSAGE.getVersion());
    }

}
