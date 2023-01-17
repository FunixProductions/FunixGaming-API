package fr.funixgaming.api.client.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;

public class FeignConfig implements RequestInterceptor {

    private final String accessToken = System.getenv("FUNIX_API_AUTH_TOKEN");

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }
}