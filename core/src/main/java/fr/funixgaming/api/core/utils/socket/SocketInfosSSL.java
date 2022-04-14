package fr.funixgaming.api.core.utils.socket;

import fr.funixgaming.api.core.exceptions.ApiException;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;

public class SocketInfosSSL {

    private final SSLServerSocketFactory sslServerSocketFactory;
    private final SSLSocketFactory sslSocketFactory;

    /**
     * Used to secure ssl sockets
     * @param password password to access jksFile
     * @param jksFile jks file generated with<br>
     *                keytool -genkey -keystore {jksFile} -keyalg RSA
     */
    public SocketInfosSSL(final String password, final File jksFile) throws ApiException {
        try {
            final char[] pass = password.toCharArray();
            final KeyStore keyStore = KeyStore.getInstance(jksFile, pass);
            final SSLContext context = getContext(keyStore, pass);

            this.sslServerSocketFactory = context.getServerSocketFactory();
            this.sslSocketFactory = context.getSocketFactory();
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | KeyManagementException e) {
            throw new ApiException("Une erreur est survenue lors de la récupération des infos SSL.", e);
        }
    }

    private SSLContext getContext(final KeyStore keyStore, final char[] pass) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("NewSunX509");
        final SSLContext context = SSLContext.getInstance("TLS");

        trustManagerFactory.init(keyStore);
        keyManagerFactory.init(keyStore, pass);
        context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return context;
    }

    public ServerSocket getServerSocket(final int port) throws ApiException {
        try {
            return this.sslServerSocketFactory.createServerSocket(port);
        } catch (IOException e) {
            throw new ApiException("Impossible de créer le socket serveur.", e);
        }
    }

    public Socket getClientSocket(final String address, final int port) throws ApiException {
        try {
            return this.sslSocketFactory.createSocket(address, port);
        } catch (IOException e) {
            throw new ApiException(String.format("Impossible de se connecter au socket %s:%d", address, port), e);
        }
    }

}
