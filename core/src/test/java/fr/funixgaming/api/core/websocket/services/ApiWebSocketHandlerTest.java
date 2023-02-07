package fr.funixgaming.api.core.websocket.services;

import fr.funixgaming.api.core.websocket.entities.WebSocketClientTest;
import fr.funixgaming.api.core.websocket.entities.WebSocketTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiWebSocketHandlerTest {

    private final WebSocketTest webSocketTest = new WebSocketTest();

    @BeforeEach
    void setupSessions() throws Exception {
        webSocketTest.afterConnectionEstablished(generateSession());
        webSocketTest.afterConnectionEstablished(generateSession());
    }

    @Test
    void sendMessageTest() throws Exception {
        final String message = "Je suis donc un message";
        webSocketTest.handleTextMessage(generateSession(), new TextMessage(message));
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
        assertEquals(message, webSocketTest.getLastMessage());
    }

    @Test
    void sendMessageToSpecificClient() throws Exception {
        final String message = "test to someone";
        final WebSocketSession session = generateSession();
        webSocketTest.afterConnectionEstablished(session);

        webSocketTest.sendMessageToSessionId(session.getId(), message);
        assertEquals(message, webSocketTest.getLastMessage());
    }

    private WebSocketSession generateSession() {
        return new WebSocketClientTest(this.webSocketTest);
    }

}
