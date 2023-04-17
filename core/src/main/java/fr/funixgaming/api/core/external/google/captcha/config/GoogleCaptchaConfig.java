package fr.funixgaming.api.core.external.google.captcha.config;

public interface GoogleCaptchaConfig {
    String getSite();
    String getSecret();
    Float getThreshold();
    boolean isDisabled();
}
