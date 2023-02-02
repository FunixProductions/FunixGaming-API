package fr.funixgaming.api.server.external_api_impl.twitch.eventsub.services;

import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.utils.string.PasswordGenerator;
import fr.funixgaming.api.core.utils.string.StringComparators;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class TwitchEventSubHmacService {

    public static final String FILE_SECRET_NAME = "twitch-eventsub-secret.key";

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String HMAC_PREFIX = "sha256=";

    public static final String TWITCH_MESSAGE_ID = "Twitch-Eventsub-Message-Id";
    public static final String TWITCH_MESSAGE_TIMESTAMP = "Twitch-Eventsub-Message-Timestamp";
    public static final String TWITCH_MESSAGE_SIGNATURE = "Twitch-Eventsub-Message-Signature";

    private final String key;
    private final Mac mac;

    public TwitchEventSubHmacService() {
        this.key = getAppSecret();
        final SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);

        try {
            this.mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKeySpec);
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new ApiException(String.format("L'lagorithme HMAC %s n'existe pas.", HMAC_ALGORITHM), noSuchAlgorithmException);
        } catch (InvalidKeyException invalidKeyException) {
            throw new ApiException("La clé d'encryption pour HMAC twitch est invalide.", invalidKeyException);
        }
    }

    /**
     * Uses this to verify that the callback is called by Twitch.
     * @param request the request to get the headers
     * @param body request body
     * @throws ApiBadRequestException when the header signature is not from twitch
     */
    public void validEventMessage(final HttpServletRequest request, final String body) throws ApiBadRequestException {
        final String twitchHmac = getHmacFromTwitch(request, body);
        final String hmac = HMAC_PREFIX + getHmac(twitchHmac);

        if (!StringComparators.timingSafeEqual(
                hmac.getBytes(StandardCharsets.UTF_8),
                request.getHeader(TWITCH_MESSAGE_SIGNATURE).getBytes(StandardCharsets.UTF_8))
        ) {
            throw new ApiBadRequestException("La clé fournie en header ne correspond pas avec la clé de la funix api.");
        }
    }

    /**
     * Method to get the key used to encode messages, twitch need this
     * @return String key
     */
    public String getKey() {
        return key;
    }

    private String getHmacFromTwitch(final HttpServletRequest request, final String body) {
        return request.getHeader(TWITCH_MESSAGE_ID) + request.getHeader(TWITCH_MESSAGE_TIMESTAMP) + body;
    }

    private String getHmac(final String message) {
        final byte[] hmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return new String(hmac, StandardCharsets.UTF_8);
    }

    private static String getAppSecret() {
        final File secretFile = new File(FILE_SECRET_NAME);
        String secret = readSecretFromFile(secretFile);

        if (secret != null) {
            return secret;
        } else {
            secret = generateNewSecret();

            try {
                if (!secretFile.exists() && !secretFile.createNewFile()) {
                    throw new ApiException("Impossible de créer le fichier qui contient la clé d'encryption twitch hmac.");
                }

                Files.writeString(
                        secretFile.toPath(),
                        secret,
                        StandardCharsets.UTF_8,
                        StandardOpenOption.TRUNCATE_EXISTING
                );
                return secret;
            } catch (IOException e) {
                throw new ApiException("Erreur lors de la création d'un nouveau fichier d'encryption twitch eventsub.", e);
            }
        }
    }

    @Nullable
    private static String readSecretFromFile(final File file) {
        if (!file.exists()) {
            return null;
        } else {
            try {
                return Files.readString(file.toPath(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new ApiException("Erreur lors de la récupération du token secret pour les event sub twitch.", e);
            }
        }
    }

    private static String generateNewSecret() {
        final PasswordGenerator passwordGenerator = new PasswordGenerator();
        passwordGenerator.setAlphaUpper(15);
        passwordGenerator.setAlphaDown(15);
        passwordGenerator.setNumbersAmount(10);
        passwordGenerator.setSpecialCharsAmount(10);

        return passwordGenerator.generateRandomPassword();
    }
}
