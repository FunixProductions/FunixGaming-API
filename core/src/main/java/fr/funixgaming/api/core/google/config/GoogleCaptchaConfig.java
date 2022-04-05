package fr.funixgaming.api.core.google.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class GoogleCaptchaConfig {
    private String site;
    private String secret;
    private float thresold;
}