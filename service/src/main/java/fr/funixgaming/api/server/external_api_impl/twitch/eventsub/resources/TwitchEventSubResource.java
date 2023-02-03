package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.resources;


import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services.TwitchEventSubService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/eventsub")
@RequiredArgsConstructor
public class TwitchEventSubResource {

    private final TwitchEventSubService twitchEventSubService;

    @PostMapping("cb")
    public String handleTwitchCallback(@RequestBody final byte[] body,
                                       final HttpServletRequest servletRequest) {
        return twitchEventSubService.handleNewWebhook(servletRequest, body);
    }

}
