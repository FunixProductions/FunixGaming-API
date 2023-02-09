package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelHypeTrainEndSubscription extends ChannelSubscription {
    public ChannelHypeTrainEndSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_END.getType(), ChannelEventType.HYPE_TRAIN_END.getVersion());
    }
}