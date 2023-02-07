package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.enums;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel.ChannelFollowSubscription;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel.ChannelSubscription;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.dtos.requests.channel.ChannelUpdateSubscription;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChannelEventType {
    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelfollow">Doc</a>
     */
    FOLLOW("channel.follow", "beta", ChannelFollowSubscription.class),

    /**
     * <a href="https://dev.twitch.tv/docs/eventsub/eventsub-subscription-types/#channelupdate">Doc</a>
     */
    UPDATE("channel.update", "1", ChannelUpdateSubscription.class);

    private final String type;
    private final String version;
    private final Class<? extends ChannelSubscription> clazz;

}
