package fr.funixgaming.api.server.user.resources;

import fr.funixgaming.api.client.user.clients.UserCrudClient;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserSecretsDTO;
import fr.funixgaming.api.core.crud.dtos.PageDTO;
import fr.funixgaming.api.server.user.services.UserCrudService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserCrudResource implements UserCrudClient {

    private final UserCrudService userCrudService;

    @Override
    public PageDTO<UserDTO> getAll(String page, String elemsPerPage, String search, String sort) {
        return userCrudService.getAll(page, elemsPerPage, search, sort);
    }

    @Override
    public UserDTO findById(String id) {
        return userCrudService.findById(id);
    }

    @Override
    public UserDTO create(UserSecretsDTO request) {
        return userCrudService.create(request);
    }

    @Override
    public UserDTO update(UserSecretsDTO request) {
        return userCrudService.update(request);
    }

    @Override
    public void delete(String id) {
        userCrudService.delete(id);
    }
}
