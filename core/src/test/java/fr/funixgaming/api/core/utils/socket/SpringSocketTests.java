package fr.funixgaming.api.core.utils.socket;

import fr.funixgaming.api.core.TestApp;
import fr.funixgaming.api.core.exceptions.ApiException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@AutoConfigureMockMvc
@SpringBootTest(classes = TestApp.class)
public class SpringSocketTests {

    private final String ip = "127.0.0.1";
    private final String passwordJKS = "springtest";
    private final int port = 2525;
    private final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    @Test
    public void testServerNoSecure() throws InterruptedException {
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

    @Test
    public void testServerSecure() throws InterruptedException {
        try {
            final File jksFile = new File("key.jks");
            final SocketInfosSSL socketInfosSSL = new SocketInfosSSL(passwordJKS, jksFile);

            final String toCheck = "oui je suis un test2";
            final TestServerSocket server = new TestServerSocket(port + 1, socketInfosSSL, atomicBoolean, toCheck);
            final TestClientSocket client = new TestClientSocket(ip, port + 1, socketInfosSSL, atomicBoolean, toCheck);

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
