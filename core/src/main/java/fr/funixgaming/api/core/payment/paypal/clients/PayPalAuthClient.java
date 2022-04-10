package fr.funixgaming.api.core.payment.paypal.clients;

import fr.funixgaming.api.core.payment.paypal.configs.PayPalAuthConfig;
import fr.funixgaming.api.core.payment.paypal.dtos.PayPalToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "PayPalAuth",
        url = "${paypal.url.auth}",
        path = "/v1/oauth2/token",
        configuration = PayPalAuthConfig.class
)
public interface PayPalAuthClient {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    PayPalToken getToken(@RequestBody String body);

}
