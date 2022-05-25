package fr.funixgaming.api.client.user.clients;

import fr.funixgaming.api.client.config.FunixApiAuthConfig;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserAdminDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(
        name = "UserCrud",
        url = "${funix.api.app-domain-url}",
        path = "/user",
        configuration = FunixApiAuthConfig.class
)
public interface UserCrudClient {
    @GetMapping
    List<UserDTO> getAll(@RequestParam(value = "page", defaultValue = "0") String page,
                         @RequestParam(value = "elemsPerPage", defaultValue = "300") String elemsPerPage);

    @GetMapping("{id}")
    UserDTO findById(@PathVariable("id") String id);

    @PostMapping
    UserDTO create(@RequestBody @Valid UserAdminDTO request);

    @PatchMapping
    UserDTO update(@RequestBody UserAdminDTO request);

    @DeleteMapping
    void delete(@RequestParam String id);
}
