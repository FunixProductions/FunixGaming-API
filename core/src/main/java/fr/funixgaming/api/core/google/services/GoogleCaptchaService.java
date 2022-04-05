package fr.funixgaming.api.core.google.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.google.clients.GoogleCaptchaClient;
import fr.funixgaming.api.core.google.config.GoogleCaptchaConfig;
import fr.funixgaming.api.core.google.dtos.GoogleCaptchaSiteVerifyResponse;
import fr.funixgaming.api.core.google.dtos.GoogleCaptchaVerifyRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Service
public class GoogleCaptchaService {
    private static final int MAX_ATTEMPT = 8;
    private static final String HTTP_GOOGLE_CAPTCHA_PARAMETER = "response";
    private static final String REGISTER_ACTION = "register";
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
                    public Integer load(String s) {
                        return 0;
                    }
                });
    }

    public void checkCode(final HttpServletRequest request) {
        final String captchaCode = request.getParameter(HTTP_GOOGLE_CAPTCHA_PARAMETER);
        final String clientIp = request.getRemoteAddr();

        if (isBlocked(clientIp)) {
            throw new ApiForbiddenException(String.format("Vous avez fait plus de %d essais. Vous êtes donc bloqué pendant quelques heures. Veuillez réessayer plus tard.", MAX_ATTEMPT));
        }

        if (StringUtils.hasLength(captchaCode) && RESPONSE_PATTERN.matcher(captchaCode).matches()) {
            final GoogleCaptchaVerifyRequest googleRequest = new GoogleCaptchaVerifyRequest();
            googleRequest.setSecret(googleCaptchaConfig.getSecret());
            googleRequest.setResponse(captchaCode);
            googleRequest.setRemoteip(clientIp);

            final GoogleCaptchaSiteVerifyResponse response = googleCaptchaClient.verify(googleRequest);
            if (response.isSuccess() && response.getAction().equals(REGISTER_ACTION) && response.getScore() > googleCaptchaConfig.getThresold()) {
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
