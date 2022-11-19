package fr.funixgaming.api.server.external_api_impl.twitch.reference.clients;

import fr.funixgaming.api.server.external_api_impl.twitch.reference.dtos.TwitchChannelDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "TwitchReferenceClient",
        url = "${twitch.api.app-api-domain-url}",
        configuration = TwitchReferenceRequestInterceptor.class,
        path = "helix"
)
public interface TwitchReferenceClient {

    /**
     * <p>Gets information about one or more channels.</p>
     * @param twitchAccessToken Bearer {accessToken}
     * @param broadcasterId The ID of the broadcaster whose channel you want to get. Can be multiple
     * @return Channel information
     */
    @GetMapping("channels")
    List<TwitchChannelDTO> getChannelInformation(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String twitchAccessToken,
                                                 @RequestParam(name = "broadcaster_id") String... broadcasterId);

}
