package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelPredictionBeginSubscription extends ChannelSubscription {
    public ChannelPredictionBeginSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_BEGIN.getType(), ChannelEventType.PREDICTION_BEGIN.getVersion());
    }
}
