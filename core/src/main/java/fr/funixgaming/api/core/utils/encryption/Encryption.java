package fr.funixgaming.api.core.utils.encryption;

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
    private static final String CRYPT_ALGORITHM = "AES";

    private final Base64.Encoder base64Encoder;
    private final Base64.Decoder base64Decoder;
    private final Key key;

    public Encryption() {
        this.key = getKeyFromFile();
        this.base64Encoder = Base64.getEncoder();
        this.base64Decoder = Base64.getDecoder();
    }

    public synchronized String convertToDatabase(final String object) throws ApiException {
        try {
            if (object == null) {
                return null;
            }

            final Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            final byte[] objectBytes = object.getBytes(StandardCharsets.UTF_8);
            final byte[] encrypted = cipher.doFinal(objectBytes);
            return base64Encoder.encodeToString(encrypted);
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ApiException("Une erreur est survenue lors de l'encryption.", e);
        }
    }

    public synchronized String convertToEntity(final String dbData) throws ApiException {
        try {
            if (dbData == null) {
                return null;
            }

            final Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);

            final byte[] base64Decoded = base64Decoder.decode(dbData);
            final byte[] decrypted = cipher.doFinal(base64Decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new ApiException("Une erreur est survenue lors du décryptage.", e);
        }
    }

    private static Key getKeyFromFile() {
        final Base64.Encoder base64Encoder = Base64.getEncoder();
        final Base64.Decoder base64Decoder = Base64.getDecoder();
        final File keyFile = new File("crypt.key");

        try {
            if (!keyFile.exists()) {
                if (!keyFile.createNewFile()) {
                    throw new ApiException("Impossible de créer un nouveau fichier d'encryption.");
                }

                final KeyGenerator keyGenerator = KeyGenerator.getInstance(CRYPT_ALGORITHM);
                keyGenerator.init(256);
                final Key key = keyGenerator.generateKey();

                final String keyString = base64Encoder.encodeToString(key.getEncoded());
                try {
                    Files.writeString(keyFile.toPath(), keyString, StandardOpenOption.TRUNCATE_EXISTING);
                } catch (Exception e) {
                    if (!keyFile.delete()) {
                        throw new ApiException("Impossible de supprimer le fichier d'encryption. Lors d'une erreur.", e);
                    }
                    throw e;
                }
                return key;
            }

            final String keyString = Files.readString(keyFile.toPath(), StandardCharsets.UTF_8);
            final byte[] decodedKey = base64Decoder.decode(keyString);
            return new SecretKeySpec(decodedKey, 0, decodedKey.length, CRYPT_ALGORITHM);
        } catch (Exception e)  {
            throw new ApiException("Une erreur est survenue lors de la création du fichier d'encryption.", e);
        }
    }

    public Key getKey() {
        return key;
    }
}
