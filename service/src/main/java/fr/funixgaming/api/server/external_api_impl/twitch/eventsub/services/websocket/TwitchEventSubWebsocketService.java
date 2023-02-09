package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services.websocket;

import com.google.gson.Gson;
import fr.funixgaming.api.client.external_api_impl.twitch.eventsub.dtos.websocket.TwitchEventSubWebsocketMessage;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.websocket.services.ApiWebsocketServerHandler;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.entities.TwitchClientToken;
import fr.funixgaming.api.server.external_api_impl.twitch.auth.repositories.TwitchClientTokenRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service who handles websocket data sending
 */
@Slf4j
@Service
public class TwitchEventSubWebsocketService extends ApiWebsocketServerHandler {

    public static final String LISTEN_CALL_CLIENT = "listen";

    private final Gson gson = new Gson();
    private final TwitchClientTokenRepository clientTokenRepository;

    public TwitchEventSubWebsocketService(TwitchClientTokenRepository repository) {
        this.clientTokenRepository = repository;
    }

    private final Map<String, String> sessionsMapsStreamersEvents = new HashMap<>();

    public void newNotification(final String notificationType, final String streamerId, final String data) {
        final TwitchEventSubWebsocketMessage eventSubWebsocketMessage = new TwitchEventSubWebsocketMessage();
        eventSubWebsocketMessage.setStreamerId(streamerId);
        eventSubWebsocketMessage.setNotificationType(notificationType);
        eventSubWebsocketMessage.setEvent(data);
        final String message = gson.toJson(eventSubWebsocketMessage);

        for (final Map.Entry<String, String> entry : this.sessionsMapsStreamersEvents.entrySet()) {
            final String streamerIdEntry = entry.getValue();

            if (streamerIdEntry.equals(streamerId)) {
                final String sessionId = entry.getKey();

                try {
                    super.sendMessageToSessionId(sessionId, message);
                } catch (Exception e) {
                    log.error("Impossible d'envoyer un message websocket à la session id {}.", sessionId, e);
                }
            }
        }
    }

    @Override
    protected void newWebsocketMessage(@NonNull WebSocketSession session, @NonNull String message) throws Exception {
        if (message.startsWith(LISTEN_CALL_CLIENT)) {
            final String[] data = message.split(":");
            if (data.length == 2) {
                final String streamerUsername = data[1];
                final Optional<TwitchClientToken> token = clientTokenRepository.findTwitchClientTokenByTwitchUsernameEqualsIgnoreCase(streamerUsername);

                if (token.isPresent()) {
                    this.sessionsMapsStreamersEvents.put(session.getId(), token.get().getTwitchUsername());
                } else {
                    throw new ApiBadRequestException(String.format("Le streamer %s n'existe pas sur la funix api.", streamerUsername));
                }
            }
        }
    }

    @Override
    protected void onClientDisconnect(String sessionId) {
        this.sessionsMapsStreamersEvents.remove(sessionId);
    }

}
