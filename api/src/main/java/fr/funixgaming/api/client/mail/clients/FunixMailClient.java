package fr.funixgaming.api.client.mail.clients;

import fr.funixgaming.api.client.config.FunixApiAuthConfig;
import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "FunixMail",
        url = "${funix.api.app-domain-url}",
        path = "/mail/",
        configuration = FunixApiAuthConfig.class

)
public interface FunixMailClient {

    @PostMapping
    FunixMailDTO sendMail(@RequestBody FunixMailDTO request);

    @GetMapping("{id}")
    FunixMailDTO getMailById(@PathVariable String id);

}
