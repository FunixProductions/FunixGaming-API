package fr.funixgaming.api.server.external_api_impl.google.clients;

import fr.funixgaming.api.server.external_api_impl.google.dtos.GoogleCaptchaSiteVerifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "GoogleCaptchaClient", url = "${google.domain}", path = "/recaptcha/api")
public interface GoogleCaptchaClient {

    @PostMapping("siteverify")
    GoogleCaptchaSiteVerifyResponse verify(@RequestParam("secret") String secret,
                                           @RequestParam("response") String response,
                                           @RequestParam("remoteip") String remoteIp,
                                           @RequestBody String body);

}
