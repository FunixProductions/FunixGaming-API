package fr.funixgaming.api.server.user.services;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserSecretsDTO;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.server.user.components.UserPasswordUtils;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.mappers.UserMapper;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserCrudService extends ApiService<UserDTO, User, UserMapper, UserRepository> implements UserDetailsService {

    private final UserTokenService tokenService;
    private final UserPasswordUtils passwordUtils;

    public UserCrudService(UserRepository repository,
                           UserMapper mapper,
                           UserTokenService tokenService,
                           UserPasswordUtils passwordUtils) {
        super(repository, mapper);
        this.tokenService = tokenService;
        this.passwordUtils = passwordUtils;
    }

    @Override
    public void beforeSavingEntity(@NonNull UserDTO request, @NonNull User entity) {
        if (request.getId() == null) {
            final Optional<User> search = this.getRepository().findByUsername(request.getUsername());
            if (search.isPresent()) {
                throw new ApiBadRequestException(String.format("L'utilisateur %s existe déjà.", request.getUsername()));
            }
        }

        if (request instanceof final UserSecretsDTO secretsDTO && Strings.isNotBlank(secretsDTO.getPassword())) {
            passwordUtils.checkPassword(secretsDTO.getPassword());
            entity.setPassword(secretsDTO.getPassword());
            tokenService.invalidTokens(request.getId());
        }
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return super.getRepository()
                .findByUsername(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("Utilisateur %s non trouvé", username))
                );
    }
}
