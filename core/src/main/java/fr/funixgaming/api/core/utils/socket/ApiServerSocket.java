package fr.funixgaming.api.core.utils.socket;

import fr.funixgaming.api.core.exceptions.ApiException;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public abstract class ApiServerSocket {

    private final ServerSocket serverSocket;
    private final Set<Socket> clients = new HashSet<>();

    public ApiServerSocket(final int port) throws ApiException {
        try {
            this.serverSocket = new ServerSocket(port);
            new Thread(this::worker);
        } catch (IOException e) {
            throw new ApiException("Une erreur est surveue lors de la création du socket serveur.", e);
        }
    }

    public ApiServerSocket(final int port, final SocketInfosSSL ssl) throws ApiException {
        try {
            final ServerSocketFactory serverSocketFactory = SSLServerSocketFactory.getDefault();
            this.serverSocket = serverSocketFactory.createServerSocket(port);
        } catch (IOException e) {
            throw new ApiException("Une erreur est surveue lors de la création du socket serveur SSL.", e);
        }
    }

    public void closeServer() throws ApiException {
        try {
            for (final Socket socket : clients) {
                if (!socket.isClosed()) {
                    socket.close();
                }
            }
        } catch (IOException e) {
            throw new ApiException("Une erreur est survenue lors de la fermeture du socket client.", e);
        }

        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new ApiException("Une erreur est survenue lors de la fermeture du socket serveur.", e);
        }
    }

    private void worker() {
        while (!this.serverSocket.isClosed()) {
            try {
                final Socket socket = this.serverSocket.accept();

                this.newClient(socket);
                this.clients.add(socket);
                clients.removeIf(Socket::isClosed);
            } catch (IOException e) {
                new ApiException("Une erreur est survenue lors de l'execution du socket client.", e).printStackTrace();
            }
        }
    }

    public abstract void newClient(final Socket socket);
}
