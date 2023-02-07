package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service who handles websocket data sending
 */
@Service
public class TwitchEventSubWebsocketService extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final Map<String, String> sessionsMapsStreamersEvents = new HashMap<>();

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        if (session.isOpen()) {
            this.sessions.add(session);
        }
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    public void newChannelNotification(final String notificationType, final String streamerId, final String data) {

    }
}
