package fr.funixgaming.api.core.utils.socket;

import fr.funixgaming.api.core.exceptions.ApiException;
import lombok.Getter;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class TestServerSocket extends ApiServerSocket {
    private final Set<TestClientSocket> clientSockets = new HashSet<>();
    private final AtomicBoolean passed;
    private final String toCheck;

    public TestServerSocket(int port, final AtomicBoolean atomicBoolean, final String toCheck) throws ApiException {
        super(port);
        this.passed = atomicBoolean;
        this.toCheck = toCheck;
    }

    public TestServerSocket(int port, SocketInfosSSL ssl, final AtomicBoolean atomicBoolean, final String toCheck) throws ApiException {
        super(port, ssl);
        this.passed = atomicBoolean;
        this.toCheck = toCheck;
    }

    @Override
    public void newClient(Socket socket) {
        final TestClientSocket client = new TestClientSocket(socket, passed, toCheck);
        client.sendMessage(toCheck);
        clientSockets.add(client);
    }
}
