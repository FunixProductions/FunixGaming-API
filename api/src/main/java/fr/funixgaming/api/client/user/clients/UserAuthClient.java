package fr.funixgaming.api.client.user.clients;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.annotation.Nullable;
import javax.validation.Valid;

@FeignClient(
        name = "UserAuth",
        url = "${funix.api.app-domain-url}",
        path = "/user/"
)
public interface UserAuthClient {

    @PostMapping("register")
    UserDTO register(@RequestBody @Valid UserCreationDTO request, @RequestHeader("google_reCaptcha") @Nullable String captchaCode);

    @PostMapping("login")
    UserTokenDTO login(@RequestBody @Valid UserLoginDTO request, @RequestHeader("google_reCaptcha") @Nullable String captchaCode);

    @GetMapping("valid")
    void valid(@RequestHeader("Authorization") String token);

}
