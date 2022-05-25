package fr.funixgaming.api.server.configs;

import fr.funixgaming.api.core.mail.config.ApiMailConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.mail")
public class FunixApiMailConfig extends ApiMailConfig {
}
