package fr.funixgaming.api.twitch.service.services;

import com.funixproductions.api.twitch.reference.client.clients.chat.TwitchChatClient;
import com.funixproductions.api.twitch.reference.client.clients.stream.TwitchStreamsClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream.TwitchStreamDTO;
import com.funixproductions.api.user.client.dtos.UserDTO;
import feign.FeignException;
import fr.funixgaming.api.twitch.service.configs.TwitchConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class FunixGamingTwitchStreamService {

    private final TwitchStreamsClient twitchStreamsClient;
    private final TwitchChatClient twitchChatClient;

    private final TwitchConfig twitchConfig;

    private final FunixGamingInformationService funixGamingInformationService;

    private TwitchDataResponseDTO<TwitchStreamDTO> cacheStream = new TwitchDataResponseDTO<>();
    private TwitchDataResponseDTO<TwitchChannelChattersDTO> cacheChatters = new TwitchDataResponseDTO<>();

    @Scheduled(fixedDelay = 20, timeUnit = TimeUnit.SECONDS)
    public void fetchStreamInfo() {
        try {
            this.cacheStream = twitchStreamsClient.getStreams(twitchConfig.getStreamerUsername());
        } catch (FeignException e) {
            log.error("Impossible to fetch stream info from FunixProd APi -> Twitch API. Error code: {}", e.status(), e);
        }
    }

    @Scheduled(fixedDelay = 20, timeUnit = TimeUnit.SECONDS)
    public void fetchChatters() {
        final UserDTO userDTO = funixGamingInformationService.getFunixGamingUser();
        if (userDTO == null) {
            log.error("Impossible to fetch chatters from FunixProd APi -> Twitch API. FunixGaming user is null.");
            return;
        }

        try {
            this.cacheChatters = twitchChatClient.getChannelChatters(1000, "", userDTO.getId().toString());
        } catch (FeignException e) {
            log.error("Impossible to fetch chatters from FunixProd APi -> Twitch API. Error code: {}", e.status(), e);
        }
    }

}
