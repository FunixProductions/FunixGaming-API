package fr.funixgaming.api.server.payment.paypal.configs;

import feign.auth.BasicAuthRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayPalAuthConfig {

    private final PayPalConfig payPalConfig;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(payPalConfig.getClientId(), payPalConfig.getClientSecret());
    }

}
