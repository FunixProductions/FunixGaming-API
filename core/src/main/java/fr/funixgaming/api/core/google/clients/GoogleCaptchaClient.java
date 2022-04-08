package fr.funixgaming.api.core.google.clients;

import fr.funixgaming.api.core.google.dtos.GoogleCaptchaSiteVerifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "GoogleCaptcha", url = "https://www.google.com", path = "/recaptcha/api")

public interface GoogleCaptchaClient {

    @PostMapping("siteverify")
    GoogleCaptchaSiteVerifyResponse verify(@RequestParam("secret") String secret,
                                           @RequestParam("response") String response,
                                           @RequestParam("remoteip") String remoteIp,
                                           @RequestBody String body);

}
