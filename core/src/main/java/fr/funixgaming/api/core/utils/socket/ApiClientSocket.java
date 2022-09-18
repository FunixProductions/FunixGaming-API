package fr.funixgaming.api.core.utils.socket;

import fr.funixgaming.api.core.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public abstract class ApiClientSocket {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private boolean running = true;
    private int cooldownMessages = 0;
    private final Queue<String> messageQueue = new LinkedList<>();

    public ApiClientSocket(final String socketAddress, final int port, final SocketInfosSSL secure) throws ApiException {
        new Thread(() -> this.worker(socketAddress, port, secure)).start();
    }

    public ApiClientSocket(final String socketAddress, final int port) throws ApiException {
        new Thread(() -> this.worker(socketAddress, port, null)).start();
    }

    public ApiClientSocket(final Socket socket) throws ApiException {
        try {
            this.socket = socket;
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            new Thread(this::runSocket).start();
        } catch (IOException e) {
            throw new ApiException("La création du socket à échoué.", e);
        }
    }

    public void sendMessage(final String message) {
        if (!Strings.isEmpty(message)) {
            this.messageQueue.add(message);
        }
    }

    @Nullable
    public Socket getSocket() {
        return socket;
    }

    public void setCooldownMessages(final int cooldown) {
        this.cooldownMessages = cooldown;
    }

    public void close() {
        try {
            this.running = false;

            if (this.writer != null) {
                this.writer.close();
            }

            if (this.reader != null) {
                this.reader.close();
            }

            if (this.socket != null && !this.socket.isClosed()) {
                this.socket.close();
            }
        } catch (IOException ignored) {
        }
    }

    private void worker(final String socketAddress, final int port, @Nullable final SocketInfosSSL secure) {
        while (this.running) {

            try {
                if (secure != null) {
                    this.socket = secure.getClientSocket(socketAddress, port);
                } else {
                    this.socket = new Socket(socketAddress, port);
                }

                this.writer = new PrintWriter(this.socket.getOutputStream(), true);
                this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                runSocket();
            } catch (IOException e) {
                log.error("Une erreur est survenue lors de la connexion au socket. Erreur: {} Host: {} Port: {} Secure: {}", e.getMessage(), this.socket.getInetAddress(), this.socket.getPort(), secure != null);
            }

        }
    }

    private void runSocket() {
        new Thread(this::runMessagePool).start();

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

        try {
            if (this.writer != null) {
                this.writer.close();
            }

            if (this.reader != null) {
                this.reader.close();
            }
        } catch (IOException ignored) {
        }
    }

    private void runMessagePool() {
        while (!this.socket.isClosed()) {

            if (this.socket.isConnected()) {
                final String message = this.messageQueue.poll();

                if (!Strings.isEmpty(message)) {
                    this.writer.println(message);

                    try {
                        Thread.sleep(this.cooldownMessages);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    public abstract void receiveData(final String data);
}
