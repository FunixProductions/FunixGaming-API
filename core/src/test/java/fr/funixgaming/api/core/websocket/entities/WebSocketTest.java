package fr.funixgaming.api.core.websocket.entities;

import fr.funixgaming.api.core.websocket.services.ApiWebsocketHandler;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class WebSocketTest extends ApiWebsocketHandler {

    private String lastMessage;

    @Override
    public void newWebsocketMessage(@NonNull WebSocketSession session, @NonNull String message) throws Exception {
        this.lastMessage = message;
    }
}
