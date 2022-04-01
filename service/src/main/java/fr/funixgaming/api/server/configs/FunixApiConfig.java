package fr.funixgaming.api.server.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "funix.api")
public class FunixApiConfig {
    /**
     * App secret key for encryption
     */
    private String secret;

    /**
     * Ip list who does not needs bearer token
     */
    private String[] whitelist;
}
