package fr.funixgaming.api.core.utils.socket;

import fr.funixgaming.api.core.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public abstract class ApiClientSocket {

    private final Socket socket;
    private final PrintWriter writer;
    private final BufferedReader reader;

    public ApiClientSocket(final String socketAddress, final int port, final boolean ssl) throws ApiException {
        try {
            if (ssl) {
                this.socket = SSLSocketFactory.getDefault().createSocket(socketAddress, port);
            } else {
                this.socket = new Socket(socketAddress, port);
            }

            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            new Thread(this::worker);
        } catch (IOException e) {
            throw new ApiException("La création du socket à échoué.", e);
        }
    }

    public ApiClientSocket(final Socket socket) throws ApiException {
        try {
            this.socket = socket;
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            new Thread(this::worker);
        } catch (IOException e) {
            throw new ApiException("La création du socket à échoué.", e);
        }
    }

    public void sendMessage(final String message) {
        new Thread(() -> this.writer.println(message)).start();
    }

    public Socket getSocket() {
        return socket;
    }

    public void close() {
        try {
            this.writer.close();
            this.reader.close();
            this.socket.close();
        } catch (IOException e) {
            log.error("Une erreur est survenue lors de la fermeture du client : {}", e.getMessage());
        }
    }

    private void worker() {
        while (!this.socket.isClosed()) {
            final String data;

            try {
                data = reader.readLine();
                this.receiveData(data);
            } catch (IOException e) {
                log.error("Socket lecture erreur {}", e.getMessage());
            }
        }
    }

    public abstract void receiveData(final String data);
}
