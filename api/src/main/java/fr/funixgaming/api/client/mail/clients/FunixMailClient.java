package fr.funixgaming.api.client.mail.clients;

import fr.funixgaming.api.client.config.FeignConfig;
import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "FunixMail",
        url = "${funix.api.app-domain-url}",
        path = "/mail/",
        configuration = FeignConfig.class
)
public interface FunixMailClient {

    @PostMapping
    FunixMailDTO sendMail(@RequestBody FunixMailDTO request);

    @GetMapping("{id}")
    FunixMailDTO getMailById(@PathVariable String id);

}
