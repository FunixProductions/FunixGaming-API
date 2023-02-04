package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.resources;


import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.clients.TwitchEventSubClient;
import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.TwitchEventSubListDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services.TwitchEventSubCallbackService;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services.TwitchEventSubReferenceService;
import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services.TwitchEventSubRegistrationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/eventsub")
@RequiredArgsConstructor
public class TwitchEventSubResource implements TwitchEventSubClient {

    private final TwitchEventSubCallbackService twitchEventSubCallbackService;
    private final TwitchEventSubReferenceService twitchEventSubReferenceService;
    private final TwitchEventSubRegistrationService twitchEventSubRegistrationService;

    @Override
    public TwitchEventSubListDTO getSubscriptions(String status, String type, String userId, String after) {
        return twitchEventSubReferenceService.getSubscriptions(status, type, userId, after);
    }

    @Override
    public void deleteSubscription(String streamerUsername) {
        twitchEventSubRegistrationService.removeSubscription(streamerUsername);
    }

    @Override
    public void createSubscription(String streamerUsername) {
        twitchEventSubRegistrationService.createSubscription(streamerUsername);
    }

    @PostMapping("cb")
    public String handleTwitchCallback(@RequestBody final byte[] body,
                                       final HttpServletRequest servletRequest) {
        return twitchEventSubCallbackService.handleNewWebhook(servletRequest, body);
    }

}
