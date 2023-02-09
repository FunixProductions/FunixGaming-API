package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelRewardGetSubscription extends ChannelSubscription {

    public ChannelRewardGetSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.POINTS_REWARD_GET.getType(), ChannelEventType.POINTS_REWARD_GET.getVersion());
    }

}
