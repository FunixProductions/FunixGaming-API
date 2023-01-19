package fr.funixgaming.api.server.external_api_impl.google.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.utils.network.IPUtils;
import fr.funixgaming.api.server.external_api_impl.google.clients.GoogleCaptchaClient;
import fr.funixgaming.api.server.external_api_impl.google.config.GoogleCaptchaConfig;
import fr.funixgaming.api.server.external_api_impl.google.dtos.GoogleCaptchaSiteVerifyResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Getter
@Service
public class GoogleCaptchaService {
    private static final int MAX_ATTEMPT = 8;
    private static final String REGISTER_ACTION = "register";
    private static final String LOGIN_ACTION = "login";
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    private final GoogleCaptchaConfig googleCaptchaConfig;
    private final GoogleCaptchaClient googleCaptchaClient;
    private final LoadingCache<String, Integer> triesCache;
    private final IPUtils ipUtils;

    public GoogleCaptchaService(GoogleCaptchaConfig captchaConfig,
                                GoogleCaptchaClient captchaClient,
                                IPUtils ipUtils) {
        this.googleCaptchaConfig = captchaConfig;
        this.googleCaptchaClient = captchaClient;
        this.ipUtils = ipUtils;

        this.triesCache = CacheBuilder.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES).build(new CacheLoader<>() {
                    @Override
                    @NonNull
                    public Integer load(@NonNull String s) {
                        return 0;
                    }
                });
    }

    public void checkCode(final HttpServletRequest request,
                          final String captchaCode) {
        if (googleCaptchaConfig.isDisabled()) {
            return;
        }

        final String clientIp = ipUtils.getClientIp(request);

        if (isBlocked(clientIp)) {
            throw new ApiForbiddenException(String.format("Vous avez fait plus de %d essais. Vous êtes donc bloqué 15 minutes. Veuillez réessayer plus tard.", MAX_ATTEMPT));
        }

        if (StringUtils.hasLength(captchaCode) && RESPONSE_PATTERN.matcher(captchaCode).matches()) {
            final GoogleCaptchaSiteVerifyResponse response = googleCaptchaClient.verify(
                    googleCaptchaConfig.getSecret(),
                    captchaCode,
                    clientIp,
                    " "
            );

            if (response != null && response.isSuccess() && (response.getAction().equals(REGISTER_ACTION) || response.getAction().equals(LOGIN_ACTION)) && response.getScore() > googleCaptchaConfig.getThreshold()) {
                reCaptchaSucceeded(clientIp);
            } else {
                reCaptchaFailed(clientIp);
                throw new ApiBadRequestException("Le code google reCaptcha est invalide.");
            }
        } else {
            throw new ApiBadRequestException("Le code google reCaptcha est invalide. (match invalide)");
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
