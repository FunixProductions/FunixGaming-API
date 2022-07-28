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
     * Ip list who can do some actions
     */
    private String[] ipWhitelist = new String[0];

    /**
     * Disable whitelist, used in dev mode
     */
    private boolean disableWhitelist = false;

    private boolean proxied = true;
}
