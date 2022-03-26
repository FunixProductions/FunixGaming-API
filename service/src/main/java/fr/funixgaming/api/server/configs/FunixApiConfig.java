package fr.funixgaming.api.server.configs;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "funix.api")
public class FunixApiConfig {
    /**
     * App secret key for encryption
     */
    private String secret;
}
