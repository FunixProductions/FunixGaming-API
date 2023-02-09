package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums.ChannelEventType;

public class ChannelPredictionEndSubscription extends ChannelSubscription {
    public ChannelPredictionEndSubscription(final String streamerId) {
        super(streamerId, ChannelEventType.PREDICTION_END.getType(), ChannelEventType.PREDICTION_END.getVersion());
    }
}
