package fr.funixgaming.api.twitch.service.services;

import com.funixproductions.api.twitch.reference.client.clients.stream.TwitchStreamsClient;
import com.funixproductions.api.twitch.reference.client.dtos.responses.TwitchDataResponseDTO;
import com.funixproductions.api.twitch.reference.client.dtos.responses.channel.stream.TwitchStreamDTO;
import feign.FeignException;
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
public class DrakkadesTwitchStreamService {

    private final TwitchStreamsClient twitchStreamsClient;

    private TwitchDataResponseDTO<TwitchStreamDTO> cacheStream = new TwitchDataResponseDTO<>();

    @Scheduled(fixedDelay = 20, timeUnit = TimeUnit.SECONDS)
    public void fetchStreamInfo() {
        try {
            this.cacheStream = twitchStreamsClient.getStreams("drakkades");
        } catch (FeignException e) {
            log.error("Impossible to fetch stream info from FunixProd APi -> Twitch API. Error code: {}", e.status(), e);
        }
    }

}
