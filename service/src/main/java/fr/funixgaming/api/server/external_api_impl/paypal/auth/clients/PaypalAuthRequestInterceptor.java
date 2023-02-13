package fr.funixgaming.api.server.external_api_impl.paypal.auth.clients;

import feign.auth.BasicAuthRequestInterceptor;
import fr.funixgaming.api.server.external_api_impl.paypal.config.PaypalConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class PaypalAuthRequestInterceptor {

    private final PaypalConfig paypalConfig;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(paypalConfig.getClientId(), paypalConfig.getClientSecret());
    }
}
