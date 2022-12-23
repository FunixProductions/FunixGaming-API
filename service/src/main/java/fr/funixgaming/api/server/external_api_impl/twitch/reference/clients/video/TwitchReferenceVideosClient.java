package fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.video;

import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.channel.video.TwitchChannelVideoDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.clients.TwitchReferenceRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "TwitchReferenceVideosClient",
        url = "${twitch.api.app-api-domain-url}",
        configuration = TwitchReferenceRequestInterceptor.class,
        path = "helix/videos"
)
public interface TwitchReferenceVideosClient {

    @GetMapping
    TwitchDataResponseDTO<TwitchChannelVideoDTO> getStreaemerVods(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                                  @RequestParam(name = "user_id") String streamerId,
                                                                  @RequestParam(name = "first", required = false, defaultValue = "20") Integer amountReturned,
                                                                  @RequestParam(name = "before", required = false, defaultValue = "") String before,
                                                                  @RequestParam(name = "after", required = false, defaultValue = "") String after);

}