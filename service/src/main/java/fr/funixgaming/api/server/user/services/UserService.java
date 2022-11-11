package fr.funixgaming.api.server.user.services;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserSecretsDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.mappers.UserMapper;
import fr.funixgaming.api.server.user.repositories.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class UserService extends ApiService<UserDTO, User, UserMapper, UserRepository> implements UserDetailsService {

    private final UserTokenService tokenService;

    public UserService(UserRepository repository,
                       UserMapper mapper,
                       UserTokenService tokenService) {
        super(repository, mapper);
        this.tokenService = tokenService;
    }

    @Transactional
    public UserDTO register(final UserCreationDTO userCreationDTO) {
        if (userCreationDTO.getPassword().equals(userCreationDTO.getPasswordConfirmation())) {
            final UserSecretsDTO userSecretsDTO = this.getMapper().toSecretsDto(userCreationDTO);
            userSecretsDTO.setRole(UserRole.USER);

            return super.create(userSecretsDTO);
        } else {
            throw new ApiBadRequestException("Les mots de passe ne correspondent pas.");
        }
    }

    @Transactional
    public UserDTO create(UserSecretsDTO request) {
        return super.create(request);
    }

    @Transactional
    public UserDTO update(UserSecretsDTO request) {
        return super.update(request);
    }

    @Override
    public void beforeSavingEntity(@NonNull UserDTO request, @NonNull User entity) {
        if (request.getId() == null) {
            final Optional<User> search = this.getRepository().findByUsername(request.getUsername());
            if (search.isPresent()) {
                throw new ApiBadRequestException(String.format("L'utilisateur %s existe déjà.", request.getUsername()));
            }
        }

        if (request instanceof final UserSecretsDTO secretsDTO) {
            if (Strings.isNotBlank(secretsDTO.getPassword())) {
                entity.setPassword(secretsDTO.getPassword());
                tokenService.invalidTokens(request.getId());
            }
        }
    }

    @Nullable
    public UserDTO getCurrentUser() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        final Authentication authentication = securityContext.getAuthentication();

        if (authentication == null) {
            return null;
        }

        final Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return super.getMapper().toDto((User) principal);
        }
        return null;
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
