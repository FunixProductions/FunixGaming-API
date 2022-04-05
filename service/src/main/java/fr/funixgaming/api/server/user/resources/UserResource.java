package fr.funixgaming.api.server.user.resources;

import fr.funixgaming.api.client.user.clients.UserClient;
import fr.funixgaming.api.client.user.dtos.UserAdminDTO;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserResource implements UserClient {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public UserDTO findById(String id) {
        final UserDTO userDTO = userService.getCurrentUser();
        if (userDTO == null) {
            throw new ApiException("Vous n'êtes pas connecté à l'application.");
        }

        if (id.equals("self")) {
            return userDTO;
        } else {
            if (userDTO.getRole().equals(UserRole.ADMIN)) {
                return userService.findById(id);
            } else {
                throw new ApiForbiddenException("Vous n'êtes pas admin pour effectuer cette opération.");
            }
        }
    }

    @Override
    public UserDTO register(UserCreationDTO request) {
        if (request.getPassword().equals(request.getPasswordConfirmation())) {
            return this.userService.create(request);
        } else {
            throw new ApiBadRequestException("Les mots de passe ne correspondent pas.");
        }
    }

    @Override
    public UserTokenDTO login(UserLoginDTO request) {
        try {
            final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            final Authentication authenticate = authenticationManager.authenticate(auth);
            final User user = (User) authenticate.getPrincipal();

            return userService.generateAccessToken(user);
        } catch (BadCredentialsException ex) {
            throw new ApiBadRequestException("Vos identifiants sont incorrects.", ex);
        }
    }

    @Override
    public Set<UserDTO> getAll() {
        return userService.getAll();
    }

    @Override
    public UserDTO create(UserAdminDTO request) {
        return userService.create(request);
    }

    @Override
    public UserDTO update(UserAdminDTO request) {
        return userService.update(request);
    }

    @Override
    public void delete(String id) {
        userService.delete(id);
    }
}
