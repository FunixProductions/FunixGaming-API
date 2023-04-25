package fr.funixgaming.api.core.utils.websocket.entities;

import fr.funixgaming.api.core.utils.websocket.services.ApiWebsocketServerHandler;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class WebSocketServer extends ApiWebsocketServerHandler {

    private String lastMessage;

    @Override
    public void newWebsocketMessage(@NonNull WebSocketSession session, @NonNull String message) throws Exception {
        this.lastMessage = message;
    }
}