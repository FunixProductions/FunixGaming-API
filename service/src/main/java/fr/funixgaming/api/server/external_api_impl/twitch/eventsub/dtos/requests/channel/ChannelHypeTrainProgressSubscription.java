package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelHypeTrainProgressSubscription extends ChannelSubscription {
    public ChannelHypeTrainProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.HYPE_TRAIN_PROGRESS.getType(), ChannelEventType.HYPE_TRAIN_PROGRESS.getVersion());
    }
}
