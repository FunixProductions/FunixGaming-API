package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelPredictionProgressSubscription extends ChannelSubscription {
    public ChannelPredictionProgressSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_PROGRESS.getType(), ChannelEventType.PREDICTION_PROGRESS.getVersion());
    }
}
