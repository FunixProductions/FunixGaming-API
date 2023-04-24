package fr.funixgaming.api.core.external.google.captcha.services;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.external.google.captcha.config.GoogleCaptchaConfig;
import fr.funixgaming.api.core.external.google.captcha.dtos.GoogleCaptchaSiteVerifyResponse;
import fr.funixgaming.api.core.utils.network.IPUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Getter
public class GoogleCaptchaService {
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    private static final int MAX_ATTEMPT = 8;
    private static final String CAPTCHA_CODE_HEADER = "X-Captcha-Google-Code";

    private final GoogleCaptchaConfig googleCaptchaConfig;
    private final IPUtils ipUtils;

    private final Cache<String, Integer> triesCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).build();
    private final Gson gson = new Gson();

    public GoogleCaptchaService(GoogleCaptchaConfig captchaConfig,
                                IPUtils ipUtils) {
        this.googleCaptchaConfig = captchaConfig;
        this.ipUtils = ipUtils;
    }

    public void checkCode(final @NonNull HttpServletRequest request,
                          final @NonNull String actionCode) {
        if (googleCaptchaConfig.isDisabled()) {
            return;
        }
        if (Strings.isNullOrEmpty(actionCode) || actionCode.isBlank()) {
            throw new ApiBadRequestException("Action code empty.");
        }

        final String captchaCode = request.getHeader(CAPTCHA_CODE_HEADER);
        if (captchaCode == null) {
            throw new ApiBadRequestException("Le code google reCaptcha header est invalide. (code null)");
        }
        final String clientIp = ipUtils.getClientIp(request);

        if (isBlocked(clientIp)) {
            throw new ApiForbiddenException(String.format("Vous avez fait plus de %d essais. Vous êtes donc bloqué" +
                    " 15 minutes. Veuillez réessayer plus tard.", MAX_ATTEMPT));
        }

        if (StringUtils.hasLength(captchaCode) && RESPONSE_PATTERN.matcher(captchaCode).matches()) {
            final GoogleCaptchaSiteVerifyResponse response = this.makeVerify(captchaCode, clientIp);

            if (isValidCaptcha(response, actionCode)) {
                reCaptchaSucceeded(clientIp);
            } else {
                reCaptchaFailed(clientIp);
                throw new ApiBadRequestException("Le code google reCaptcha est invalide. (api error)");
            }
        } else {
            throw new ApiBadRequestException("Le code google reCaptcha est invalide. (match invalide)");
        }
    }

    private void reCaptchaSucceeded(String key) {
        triesCache.invalidate(key);
    }

    private void reCaptchaFailed(String key) {
        Integer attempts = triesCache.getIfPresent(key);

        if (attempts == null) {
            attempts = 0;
        }
        triesCache.put(key, attempts + 1);
    }

    private boolean isBlocked(@NonNull final String ip) {
        final Integer attempts = triesCache.getIfPresent(ip);
        return attempts != null && attempts >= MAX_ATTEMPT;
    }

    @NonNull
    private GoogleCaptchaSiteVerifyResponse makeVerify(final String captchaCode,
                                                       final String remoteIp) throws ApiException {
        final HttpRequest.Builder httpRequestBuilder = HttpRequest.newBuilder()
                .uri(URI.create("https://www.google.com/recaptcha/api/siteverify" +
                        "?secret=" + googleCaptchaConfig.getSecret() + (
                        "&response=" + captchaCode +
                                "&remoteip=" + remoteIp)))
                .POST(HttpRequest.BodyPublishers.ofString(""));
        final HttpClient httpClient = HttpClient.newBuilder().build();

        try {
            final HttpRequest request = httpRequestBuilder.build();
            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            final int resStatusCode = response.statusCode();

            if (Integer.toString(resStatusCode).startsWith("2")) {
                return gson.fromJson(response.body(), GoogleCaptchaSiteVerifyResponse.class);
            } else {
                throw new ApiBadRequestException("Une erreur est survenue lors de la vérification du captcha. (status code: " + resStatusCode + ")");
            }
        } catch (IOException ioException) {
            throw new ApiException("Une erreur est survenue lors de la vérification du captcha. Veuillez recommencer. (IOException)", ioException);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException("La requête de vérification google captcha s'est interrompue, veuillez recommencer.", e);
        }
    }

    private boolean isValidCaptcha(final @NonNull GoogleCaptchaSiteVerifyResponse response, final String actionCode) {
        return response.isSuccess() &&
                response.getAction().equals(actionCode) &&
                response.getScore() > googleCaptchaConfig.getThreshold();
    }

}
