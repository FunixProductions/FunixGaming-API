package fr.funixgaming.api.server.converters;

import fr.funixgaming.api.server.configs.FunixApiConfig;
import fr.funixgaming.api.server.exceptions.FunixApiException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public abstract class Encryption<T> implements AttributeConverter<T, String> {
    private static final String AES = "AES";

    private final Base64.Encoder base64Encoder;
    private final Base64.Decoder base64Decoder;
    private final Key key;
    private final Cipher cipher;

    public Encryption(final FunixApiConfig funixApiConfig) {
        try {
            this.key = new SecretKeySpec(funixApiConfig.getSecret().getBytes(), AES);
            this.cipher = Cipher.getInstance(AES);
            this.base64Encoder = Base64.getEncoder();
            this.base64Decoder = Base64.getDecoder();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new FunixApiException("Une erreur est survenue lors du chargement de l'encryption de BDD.", e);
        }
    }

    public String convertToDatabase(final String object) throws FunixApiException {
        try {
            if (object == null) {
                return null;
            }

            cipher.init(Cipher.ENCRYPT_MODE, key);
            return base64Encoder.encodeToString(cipher.doFinal(object.getBytes()));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new FunixApiException("Une erreur est survenue lors de l'encryption.", e);
        }
    }

    public String convertToEntity(final String dbData) throws FunixApiException {
        try {
            if (dbData == null) {
                return null;
            }

            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(base64Decoder.decode(dbData)));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new FunixApiException("Une erreur est survenue lors du décryptage.", e);
        }
    }

}
