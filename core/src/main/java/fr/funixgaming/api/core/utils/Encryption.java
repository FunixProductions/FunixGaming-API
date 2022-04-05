package fr.funixgaming.api.core.utils;

import fr.funixgaming.api.core.config.ApiConfig;
import fr.funixgaming.api.core.exceptions.ApiException;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class Encryption {
    private static final String AES = "AES";

    private final Base64.Encoder base64Encoder;
    private final Base64.Decoder base64Decoder;
    private final Cipher cipher;
    private final Key key;

    public Encryption(final ApiConfig apiConfig) {
        this.base64Encoder = Base64.getEncoder();
        this.base64Decoder = Base64.getDecoder();
        this.key = getKeyFromFile(apiConfig.getKeySize());

        try {
            this.cipher = Cipher.getInstance(AES);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new ApiException("Une erreur est survenue lors de l'initialisation de la classe de cryptage.", e);
        }
    }

    public String convertToDatabase(final String object) throws ApiException {
        try {
            if (object == null) {
                return null;
            }

            cipher.init(Cipher.ENCRYPT_MODE, key);
            return base64Encoder.encodeToString(cipher.doFinal(object.getBytes()));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new ApiException("Une erreur est survenue lors de l'encryption.", e);
        }
    }

    public String convertToEntity(final String dbData) throws ApiException {
        try {
            if (dbData == null) {
                return null;
            }

            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(base64Decoder.decode(dbData)));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new ApiException("Une erreur est survenue lors du décryptage.", e);
        }
    }

    private static Key getKeyFromFile(final int keySize) {
        final Base64.Encoder base64Encoder = Base64.getEncoder();
        final Base64.Decoder base64Decoder = Base64.getDecoder();
        final File keyFile = new File("crypt.key");

        try {
            if (!keyFile.exists()) {
                if (!keyFile.createNewFile()) {
                    throw new ApiException("Impossible de créer un nouveau fichier d'encryption.");
                }

                final KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
                keyGenerator.init(keySize);
                final Key key = keyGenerator.generateKey();

                final String keyString = base64Encoder.encodeToString(key.getEncoded());
                try {
                    Files.writeString(keyFile.toPath(), keyString, StandardOpenOption.TRUNCATE_EXISTING);
                } catch (Exception e) {
                    keyFile.delete();
                    throw e;
                }
                return key;
            }

            final String keyString = Files.readString(keyFile.toPath(), StandardCharsets.UTF_8);
            final byte[] decodedKey = base64Decoder.decode(keyString);
            return new SecretKeySpec(decodedKey, 0, decodedKey.length, AES);
        } catch (Exception e)  {
            throw new ApiException("Une erreur est survenue lors de la création du fichier d'encryption.", e);
        }
    }

    public Key getKey() {
        return key;
    }
}
