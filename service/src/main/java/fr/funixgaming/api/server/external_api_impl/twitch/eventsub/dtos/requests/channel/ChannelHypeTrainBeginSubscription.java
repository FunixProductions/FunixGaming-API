package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelHypeTrainBeginSubscription extends ChannelSubscription {
    public ChannelHypeTrainBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_BEGIN.getType(), ChannelEventType.HYPE_TRAIN_BEGIN.getVersion());
    }
}
