package fr.funixgaming.api.client.payment.paypal.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "paypal")
public class PayPalConfig {
    private String clientId;
    private String clientSecret;
}
