package fr.funixgaming.api.server.user.resources;

import fr.funixgaming.api.client.user.clients.UserClient;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.resources.ApiResource;
import fr.funixgaming.api.server.user.services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserResource extends ApiResource<UserDTO, UserService> implements UserClient {
    public UserResource(UserService userService) {
        super(userService);
    }

    @Override
    public UserDTO findById(String id) {
        if (id.equals("self")) {
            final UserDTO userDTO = super.getService().getCurrentUser();

            if (userDTO == null) {
                throw new ApiException("Vous n'êtes pas connecté à l'application.");
            } else {
                return userDTO;
            }
        } else {
            return super.findById(id);
        }
    }

    @Override
    public UserDTO register(UserCreationDTO request) {
        
        return null;
    }

    @Override
    public UserTokenDTO login(UserLoginDTO request) {
        return null;
    }
}
