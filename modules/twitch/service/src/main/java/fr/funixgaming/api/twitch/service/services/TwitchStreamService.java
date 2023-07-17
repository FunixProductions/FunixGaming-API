package fr.funixgaming.api.twitch.service.services;

import com.funixproductions.api.twitch.reference.client.clients.stream.TwitchStreamsClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream.TwitchStreamDTO;
import feign.FeignException;
import fr.funixgaming.api.twitch.service.configs.TwitchConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TwitchStreamService {

    private final TwitchStreamsClient twitchStreamsClient;
    private final TwitchConfig twitchConfig;

    private TwitchDataResponseDTO<TwitchStreamDTO> cache = new TwitchDataResponseDTO<>();

    public TwitchDataResponseDTO<TwitchStreamDTO> fetchFunixStreamData() {
        return cache;
    }

    @Scheduled(fixedDelay = 20, timeUnit = TimeUnit.SECONDS)
    public void fetchStreamInfo() {
        try {
            this.cache = twitchStreamsClient.getStreams(twitchConfig.getStreamerUsername());
        } catch (FeignException e) {
            log.error("Impossible to fetch stream info from FunixProd APi -> Twitch API. Error code: {}", e.status(), e);
        }
    }

}
