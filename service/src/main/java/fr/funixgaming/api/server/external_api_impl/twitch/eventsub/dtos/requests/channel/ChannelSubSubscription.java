package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelSubSubscription extends ChannelSubscription {

    public ChannelSubSubscription(String streamerId) {
        super(streamerId, ChannelEventType.SUBSCRIPTION.getType(), ChannelEventType.SUBSCRIPTION.getVersion());
    }

}
