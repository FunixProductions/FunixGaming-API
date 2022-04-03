package fr.funixgaming.api.client.payment.paypal.clients;

import fr.funixgaming.api.client.payment.paypal.configs.PayPalAuthConfig;
import fr.funixgaming.api.client.payment.paypal.dtos.PayPalToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "PayPalAuth",
        url = "${paypal.url.auth}",
        configuration = PayPalAuthConfig.class
)
public interface PayPalAuthClient {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    PayPalToken getToken(@RequestBody String body);

}
