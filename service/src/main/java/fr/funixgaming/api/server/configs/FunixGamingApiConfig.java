package fr.funixgaming.api.server.configs;

import com.funixproductions.core.tools.network.IPUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("funixgaming.api")
public class FunixGamingApiConfig {

    /**
     * Tells the spring app to get ip client from headers or client socket
     */
    private boolean appBehindProxy = true;

    @Bean
    public IPUtils ipUtils() {
        return new IPUtils(appBehindProxy);
    }
}
