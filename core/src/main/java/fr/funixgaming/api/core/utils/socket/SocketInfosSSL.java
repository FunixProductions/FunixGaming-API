package fr.funixgaming.api.core.utils.socket;

import fr.funixgaming.api.core.exceptions.ApiException;
import lombok.Getter;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Getter
public class SocketInfosSSL {

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
            final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            final KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("NewSunX509");

            trustManagerFactory.init(keyStore);
            keyManagerFactory.init(keyStore, pass);
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException e) {
            throw new ApiException("Une erreur est survenue lors de la récupération des infos SSL.", e);
        }
    }

}
