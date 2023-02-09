package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelCheerSubscription extends ChannelSubscription {

    public ChannelCheerSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.CHEER.getType(), ChannelEventType.CHEER.getVersion());
    }

}
