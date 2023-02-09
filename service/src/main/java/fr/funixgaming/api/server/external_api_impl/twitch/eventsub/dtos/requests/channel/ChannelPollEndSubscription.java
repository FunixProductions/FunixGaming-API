package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelPollEndSubscription extends ChannelSubscription {
    public ChannelPollEndSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POLL_END.getType(), ChannelEventType.POLL_END.getVersion());
    }
}