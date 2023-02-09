package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelPollBeginSubscription extends ChannelSubscription {
    public ChannelPollBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POLL_BEGIN.getType(), ChannelEventType.POLL_BEGIN.getVersion());
    }
}
