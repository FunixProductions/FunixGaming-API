package fr.funixgaming.api.core.websocket.services;

import fr.funixgaming.api.core.websocket.entities.WebSocketTest;
import lombok.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiWebSocketHandlerTest {

    private final WebSocketTest webSocketTest = new WebSocketTest();

    @BeforeEach
    void setupSessions() throws Exception {
        webSocketTest.afterConnectionEstablished(generateSession());
        webSocketTest.afterConnectionEstablished(generateSession());
    }

    @AfterEach
    void closeSessions() throws Exception {
        for (final WebSocketSession session : webSocketTest.getSessions().values()) {
            webSocketTest.afterConnectionClosed(session, CloseStatus.NORMAL);
        }
    }

    @Test
    void sendMessageTest() throws Exception {
        final String message = "Je suis donc un message";
        webSocketTest.handleTextMessage(generateSession(), new TextMessage(message));
        Thread.sleep(1000);
        assertEquals(message, webSocketTest.getLastMessage());
        webSocketTest.handleTextMessage(generateSession(), new TextMessage("pong:blabla"));
        assertEquals(message, webSocketTest.getLastMessage());
    }

    @Test
    void sendPingRequestTest() throws Exception {
        webSocketTest.sendPingRequestsAndCheckZombies();
    }

    @Test
    void broadcastMessageTest() throws Exception {
        final String message = "test bc";
        webSocketTest.broadcastMessage(message);
        Thread.sleep(1000);
        assertEquals(message, webSocketTest.getLastMessage());
    }

    private WebSocketSession generateSession() {
        return new WebSocketSession() {
            @Override
            public @NonNull String getId() {
                return UUID.randomUUID().toString();
            }

            @Override
            public URI getUri() {
                return null;
            }

            @Override
            public @NonNull HttpHeaders getHandshakeHeaders() {
                return new HttpHeaders();
            }

            @Override
            public @NonNull Map<String, Object> getAttributes() {
                return new HashMap<>();
            }

            @Override
            public Principal getPrincipal() {
                return null;
            }

            @Override
            public InetSocketAddress getLocalAddress() {
                return null;
            }

            @Override
            public InetSocketAddress getRemoteAddress() {
                return null;
            }

            @Override
            public String getAcceptedProtocol() {
                return null;
            }

            @Override
            public void setTextMessageSizeLimit(int messageSizeLimit) {

            }

            @Override
            public int getTextMessageSizeLimit() {
                return 0;
            }

            @Override
            public void setBinaryMessageSizeLimit(int messageSizeLimit) {

            }

            @Override
            public int getBinaryMessageSizeLimit() {
                return 0;
            }

            @Override
            public @NonNull List<WebSocketExtension> getExtensions() {
                return new ArrayList<>();
            }

            @Override
            public void sendMessage(@NonNull WebSocketMessage<?> message) throws IOException {
            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public void close() throws IOException {

            }

            @Override
            public void close(@NonNull CloseStatus status) throws IOException {
            }
        };
    }

}
