package fr.funixgaming.api.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "config.api")
public class ApiConfig {
    /**
     * Keysize for encryption
     * (128, 192, or 256 bits)
     */
    private Integer keySize = 128;

    /**
     * Ip list who can do some actions
     */
    private String[] ipWhitelist = new String[0];
}
