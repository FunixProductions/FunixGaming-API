package fr.funixgaming.api.server.configs;

import fr.funixgaming.api.core.utils.network.IPUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("funix.api")
public class FunixApiConfig {

    /**
     * Tells the spring app to get ip client from headers or client socket
     */
    private boolean appBehindProxy = true;

    /**
     * Ignore the api bearer auto generation when accessing from local client (127.0.0.1 for example)
     */
    private boolean ignoreApiAccessAdmin = false;

    @Bean
    public IPUtils ipUtils() {
        return new IPUtils(appBehindProxy);
    }
}
