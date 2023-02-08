package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.configs;

import fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services.websocket.TwitchEventSubWebsocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class TwitchEventSubWebSocketConfig implements WebSocketConfigurer {

    private final TwitchEventSubWebsocketService websocketService;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(websocketService, "/ws/public/twitch/eventsub/");
    }
}
