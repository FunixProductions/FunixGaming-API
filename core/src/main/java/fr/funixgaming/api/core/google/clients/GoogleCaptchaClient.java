package fr.funixgaming.api.core.google.clients;

import fr.funixgaming.api.core.google.dtos.GoogleCaptchaSiteVerifyResponse;
import fr.funixgaming.api.core.google.dtos.requests.GoogleCaptchaVerifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "GoogleCaptcha", url = "https://www.google.com", path = "/recaptcha/api")
public interface GoogleCaptchaClient {

    @PostMapping("siteverify")
    GoogleCaptchaSiteVerifyResponse verify(@RequestBody @Valid GoogleCaptchaVerifyRequest request);

}
