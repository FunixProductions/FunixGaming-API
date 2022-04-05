package fr.funixgaming.api.client.user.clients;

import fr.funixgaming.api.client.user.dtos.requests.UserAdminDTO;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@FeignClient(name = "user", url = "${app.domain.url}", path = "/user")
public interface UserClient {

    @PostMapping("register")
    UserDTO register(@RequestBody @Valid UserCreationDTO request);

    @PostMapping("login")
    UserTokenDTO login(@RequestBody @Valid UserLoginDTO request);

    @GetMapping
    Set<UserDTO> getAll();

    @GetMapping("{id}")
    UserDTO findById(@PathVariable("id") String id);

    @PostMapping
    UserDTO create(@RequestBody @Valid UserAdminDTO request);

    @PatchMapping
    UserDTO update(@RequestBody UserAdminDTO request);

    @DeleteMapping
    void delete(@RequestParam String id);

}
