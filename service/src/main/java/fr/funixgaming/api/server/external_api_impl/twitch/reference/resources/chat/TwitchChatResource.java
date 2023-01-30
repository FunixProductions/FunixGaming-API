package fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.chat;

import fr.funixgaming.api.client.external_api_impl.twitch.auth.dtos.TwitchClientTokenDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.clients.chat.TwitchChatClient;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.requests.TwitchChatAnnouncement;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.TwitchDataResponseDTO;
import fr.funixgaming.api.client.external_api_impl.twitch.reference.dtos.responses.channel.chat.TwitchChannelChattersDTO;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.services.TwitchClientTokenService;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.resources.TwitchReferenceResource;
import fr.funixgaming.api.server.external_api_impl.twitch.reference.services.chat.TwitchReferenceChatService;
import fr.funixgaming.api.server.user.services.CurrentSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/twitch/chat")
public class TwitchChatResource extends TwitchReferenceResource implements TwitchChatClient {

    private final TwitchReferenceChatService service;

    public TwitchChatResource(CurrentSession currentSession,
                              TwitchClientTokenService tokenService,
                              TwitchReferenceChatService service) {
        super(tokenService, currentSession);
        this.service = service;
    }

    @Override
    public TwitchDataResponseDTO<TwitchChannelChattersDTO> getChannelChatters(Integer maxChattersReturned,
                                                                              String paginationCursor) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        return service.getChannelChatters(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId(),
                tokenDTO.getTwitchUserId(),
                maxChattersReturned,
                paginationCursor
        );
    }

    @Override
    public void sendChatAnnouncement(TwitchChatAnnouncement announcement) {
        final TwitchClientTokenDTO tokenDTO = super.getTwitchAuthByUserConnected();

        service.sendChatAnnouncement(
                tokenDTO.getAccessToken(),
                tokenDTO.getTwitchUserId(),
                tokenDTO.getTwitchUserId(),
                announcement
        );
    }
}
