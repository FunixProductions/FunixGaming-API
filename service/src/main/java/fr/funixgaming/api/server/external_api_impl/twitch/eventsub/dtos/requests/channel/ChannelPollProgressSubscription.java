package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelPollProgressSubscription extends ChannelSubscription {

    public ChannelPollProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POLL_PROGRESS.getType(), ChannelEventType.POLL_PROGRESS.getVersion());
    }

}
