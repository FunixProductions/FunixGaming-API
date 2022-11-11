package fr.funixgaming.api.core.utils.socket;

import fr.funixgaming.api.core.exceptions.ApiException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SpringSocketTests {

    private final String ip = "127.0.0.1";
    private final int port = 2525;
    private final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    @Test
    public void testServer() throws InterruptedException {
        try {
            final String toCheck = "oui je suis un test";
            final TestServerSocket server = new TestServerSocket(port, atomicBoolean, toCheck);
            final TestClientSocket client = new TestClientSocket(ip, port, atomicBoolean, toCheck);

            client.sendMessage(toCheck);
            Thread.sleep(3000);
            assertTrue(atomicBoolean.get());
            server.closeServer();
            client.close();
        } catch (ApiException e) {
            fail(e);
        }
    }

}
