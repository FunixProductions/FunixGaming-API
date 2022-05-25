package fr.funixgaming.api.client.user.clients;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(
        name = "UserAuth",
        url = "${funix.api.app-domain-url}",
        path = "/user"
)
public interface UserAuthClient {

    @PostMapping("register")
    UserDTO register(@RequestBody @Valid UserCreationDTO request);

    @PostMapping("login")
    ResponseEntity<UserTokenDTO> login(@RequestBody @Valid UserLoginDTO request);

    @GetMapping("valid")
    ResponseEntity<Void> valid(@RequestHeader("Authorization") String token);

}
