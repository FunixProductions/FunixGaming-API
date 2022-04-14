package fr.funixgaming.api.core.utils.socket;

import fr.funixgaming.api.core.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

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

    public ApiClientSocket(final String socketAddress, final int port, final SocketInfosSSL secure) throws ApiException {
        try {
            this.socket = secure.getClientSocket(socketAddress, port);
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            new Thread(this::worker).start();
        } catch (IOException e) {
            throw new ApiException(String.format("Impossible de se connecter au socket ssl %s:%d", socketAddress, port), e);
        }
    }

    public ApiClientSocket(final String socketAddress, final int port) throws ApiException {
        try {
            this.socket = new Socket(socketAddress, port);
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            new Thread(this::worker).start();
        } catch (IOException e) {
            throw new ApiException(String.format("Impossible de se connecter au socket %s:%d", socketAddress, port), e);
        }
    }

    public ApiClientSocket(final Socket socket) throws ApiException {
        try {
            this.socket = socket;
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            new Thread(this::worker).start();
        } catch (IOException e) {
            throw new ApiException("La création du socket à échoué.", e);
        }
    }

    public void sendMessage(final String message) {
        if (!Strings.isEmpty(message)) {
            new Thread(() -> this.writer.println(message)).start();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void close() {
        try {
            this.writer.close();
            this.reader.close();
            this.socket.close();
        } catch (IOException ignored) {
        }
    }

    private void worker() {
        while (!this.socket.isClosed()) {
            final String data;

            try {
                data = reader.readLine();

                if (data != null) {
                    this.receiveData(data);
                }
            } catch (IOException e) {
                log.error("SocketClient: lecture erreur {}", e.getMessage());
            }
        }
    }

    public abstract void receiveData(final String data);
}
