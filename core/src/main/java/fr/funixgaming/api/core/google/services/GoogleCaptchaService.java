package fr.funixgaming.api.core.google.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.google.clients.GoogleCaptchaClient;
import fr.funixgaming.api.core.google.config.GoogleCaptchaConfig;
import fr.funixgaming.api.core.google.dtos.GoogleCaptchaSiteVerifyResponse;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Getter
@Service
public class GoogleCaptchaService {
    private static final int MAX_ATTEMPT = 8;
    private static final String HTTP_GOOGLE_CAPTCHA_PARAMETER = "google_reCaptcha";
    private static final String REGISTER_ACTION = "register";
    private static final String LOGIN_ACTION = "login";
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    private final GoogleCaptchaConfig googleCaptchaConfig;
    private final GoogleCaptchaClient googleCaptchaClient;
    private final LoadingCache<String, Integer> triesCache;

    public GoogleCaptchaService(GoogleCaptchaConfig captchaConfig,
                                GoogleCaptchaClient captchaClient) {
        this.googleCaptchaConfig = captchaConfig;
        this.googleCaptchaClient = captchaClient;

        this.triesCache = CacheBuilder.newBuilder()
                .expireAfterWrite(4, TimeUnit.HOURS).build(new CacheLoader<>() {
                    @Override
                    @NonNull
                    public Integer load(@NonNull String s) {
                        return 0;
                    }
                });
    }

    public void checkCode(final HttpServletRequest request) {
        final String captchaCode = request.getHeader(HTTP_GOOGLE_CAPTCHA_PARAMETER);
        final String clientIp = request.getRemoteAddr();

        if (isBlocked(clientIp)) {
            throw new ApiForbiddenException(String.format("Vous avez fait plus de %d essais. Vous êtes donc bloqué pendant quelques heures. Veuillez réessayer plus tard.", MAX_ATTEMPT));
        }

        if (StringUtils.hasLength(captchaCode) && RESPONSE_PATTERN.matcher(captchaCode).matches()) {
            final GoogleCaptchaSiteVerifyResponse response = googleCaptchaClient.verify(
                    googleCaptchaConfig.getSecret(),
                    captchaCode,
                    clientIp,
                    " "
            );

            if (response.isSuccess() &&
                    (response.getAction().equals(REGISTER_ACTION) || response.getAction().equals(LOGIN_ACTION)) &&
                    response.getScore() > googleCaptchaConfig.getThreshold()) {
                reCaptchaSucceeded(clientIp);
            } else {
                reCaptchaFailed(clientIp);
                throw new ApiBadRequestException("Le code google reCaptcha est invalide.");
            }
        } else {
            throw new ApiBadRequestException("Le code google reCaptcha est invalide.");
        }
    }

    private void reCaptchaSucceeded(String key) {
        triesCache.invalidate(key);
    }

    private void reCaptchaFailed(String key) {
        int attempts = triesCache.getUnchecked(key);
        triesCache.put(key, attempts + 1);
    }

    private boolean isBlocked(String key) {
        return triesCache.getUnchecked(key) >= MAX_ATTEMPT;
    }

}
