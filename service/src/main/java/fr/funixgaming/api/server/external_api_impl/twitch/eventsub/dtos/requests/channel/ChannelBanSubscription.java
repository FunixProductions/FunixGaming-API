package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelBanSubscription extends ChannelSubscription {
    public ChannelBanSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.BAN.getType(), ChannelEventType.BAN.getVersion());
    }
}
