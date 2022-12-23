package fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.stream;

import fr.funixgaming.api.client.external_api_impl.twitch.auth.dtos.TwitchClientTokenDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.auth.enums.TwitchClientTokenType;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.clients.stream.TwitchStreamsClient;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.channel.stream.TwitchStreamDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchClientTokenService;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchServerTokenService;
import fr.funixgaming.api.server.external_api_impl.twitch.configs.TwitchApiConfig;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.TwitchReferenceResource;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.stream.TwitchReferenceStreamService;
import fr.funixgaming.api.server.user.services.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/twitch/streams")
public class TwitchStreamResource extends TwitchReferenceResource implements TwitchStreamsClient {

    private final TwitchReferenceStreamService streamService;
    private final TwitchServerTokenService serverTokenService;
    private final TwitchApiConfig twitchApiConfig;

    private final TwitchDataResponseDTO<TwitchStreamDTO> funixStreamCache = new TwitchDataResponseDTO<>();

    public TwitchStreamResource(UserService userService,
                                TwitchClientTokenService clientTokenService,
                                TwitchReferenceStreamService streamService,
                                TwitchServerTokenService serverTokenService,
                                TwitchApiConfig twitchApiConfig) {
        super(userService, clientTokenService);
        this.streamService = streamService;
        this.serverTokenService = serverTokenService;
        this.twitchApiConfig = twitchApiConfig;

        fetchFunixStreamData();
    }

    @Override
    public TwitchDataResponseDTO<TwitchStreamDTO> getStreams(String streamerName) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected(TwitchClientTokenType.VIEWER);

        return streamService.getStreams(
                tokenDTO.getAccessToken(),
                streamerName
        );
    }

    @Override
    public TwitchDataResponseDTO<TwitchStreamDTO> getFunixStream() {
        return funixStreamCache;
    }

    @Scheduled(fixedRate = 15, timeUnit = TimeUnit.SECONDS)
    public void fetchFunixStreamData() {
        final TwitchDataResponseDTO<TwitchStreamDTO> funixStreamStatus = streamService.getStreams(
                serverTokenService.getAccessToken(),
                twitchApiConfig.getStreamerUsername()
        );

        this.funixStreamCache.setData(funixStreamStatus.getData());
        this.funixStreamCache.setPagination(funixStreamStatus.getPagination());
        this.funixStreamCache.setTotal(funixStreamStatus.getTotal());
    }
}
