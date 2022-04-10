package fr.funixgaming.api.core.payment.paypal.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "paypal")
/**
 * paypal.url.auth=https://api-m.sandbox.paypal.com
 * paypal.client-id=${PAYPAL_CLIENT_ID}
 * paypal.client-secret=${PAYPAL_CLIENT_SECRET}
 */
public class PayPalConfig {
    private String clientId;
    private String clientSecret;
}
