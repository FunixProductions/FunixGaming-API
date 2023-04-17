package fr.funixgaming.api.server.external_api_impl.google.captcha.config;

import fr.funixgaming.api.core.external.google.captcha.config.GoogleCaptchaConfig;
import fr.funixgaming.api.core.external.google.captcha.services.GoogleCaptchaService;
import fr.funixgaming.api.core.utils.network.IPUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("google.recaptcha.key")
public class FunixAPIGoogleCaptchaConfig implements GoogleCaptchaConfig {

    private String site;

    private String secret;

    private Float threshold;

    private boolean disabled;

    @Bean
    public GoogleCaptchaService googleCaptchaService(IPUtils ipUtils) {
        return new GoogleCaptchaService(this, ipUtils);
    }

}
