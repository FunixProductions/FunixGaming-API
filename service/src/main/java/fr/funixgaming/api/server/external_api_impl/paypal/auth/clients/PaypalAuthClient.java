package fr.funixgaming.api.server.external_api_impl.paypal.auth.clients;

import feign.Headers;
import fr.funixgaming.api.server.external_api_impl.paypal.auth.dtos.PaypalTokenAuth;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "PaypalAuthClient",
        url = "${paypal.paypal-domain}",
        path = "/v1/oauth2",
        configuration = PaypalAuthRequestInterceptor.class
)
public interface PaypalAuthClient {

    /**
     * Generate a new accessToken for paypal api
     * @param grantType client_credentials
     * @return accessToken data
     */
    @PostMapping("token")
    @Headers(HttpHeaders.CONTENT_TYPE + ": " + MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    PaypalTokenAuth getToken(@RequestParam(value = "grant_type", defaultValue = "client_credentials") String grantType);

}