package fr.funixgaming.api.client.user.clients;

import fr.funixgaming.api.client.config.FunixApiAuthConfig;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserSecretsDTO;
import fr.funixgaming.api.core.crud.dtos.PageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(
        name = "UserCrud",
        url = "${funix.api.app-domain-url}",
        path = "/user/",
        configuration = FunixApiAuthConfig.class
)
public interface UserCrudClient {
    @GetMapping
    PageDTO<UserDTO> getAll(@RequestParam(value = "page", defaultValue = "0") String page,
                            @RequestParam(value = "elemsPerPage", defaultValue = "300") String elemsPerPage,
                            @RequestParam(value = "search", defaultValue = "") String search,
                            @RequestParam(value = "sort", defaultValue = "") String sort);

    @GetMapping("{id}")
    UserDTO findById(@PathVariable("id") String id);

    @PostMapping
    UserDTO create(@RequestBody @Valid UserSecretsDTO request);

    @PatchMapping
    UserDTO update(@RequestBody UserSecretsDTO request);

    @DeleteMapping
    void delete(@RequestParam String id);
}
