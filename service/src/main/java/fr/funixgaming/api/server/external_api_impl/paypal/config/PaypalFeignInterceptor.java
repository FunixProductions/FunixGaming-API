package fr.funixgaming.api.server.external_api_impl.paypal.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import fr.funixgaming.api.server.external_api_impl.paypal.auth.services.PaypalAccessTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor
public class PaypalFeignInterceptor implements RequestInterceptor {

    private final PaypalAccessTokenService service;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + service.getAccessToken());
    }
}
