package fr.funixgaming.api.server.user.services;

import fr.funixgaming.api.client.config.FunixApiConfig;
import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserSecretsDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.utils.network.IPUtils;
import fr.funixgaming.api.core.utils.string.PasswordGenerator;
import fr.funixgaming.api.server.mail.services.FunixMailService;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.mappers.UserMapper;
import fr.funixgaming.api.server.user.repositories.UserRepository;
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

    private final FunixMailService mailService;
    private final UserTokenService tokenService;

    private final FunixApiConfig apiConfig;
    private final IPUtils ipUtils;

    public UserService(UserRepository repository,
                       UserMapper mapper,
                       FunixMailService funixMailService,
                       UserTokenService tokenService,
                       FunixApiConfig apiConfig,
                       IPUtils ipUtils) {
        super(repository, mapper);
        this.mailService = funixMailService;
        this.apiConfig = apiConfig;
        this.tokenService = tokenService;
        this.ipUtils = ipUtils;

        sendApiUserMail();
    }

    @Transactional
    public UserDTO register(final UserCreationDTO userCreationDTO) {
        if (!userCreationDTO.getPassword().equals(userCreationDTO.getPasswordConfirmation())) {
            throw new ApiBadRequestException("Les mots de passe ne correspondent pas.");
        }

        final Optional<User> search = this.getRepository().findByUsername(userCreationDTO.getUsername());
        if (search.isPresent()) {
            throw new ApiBadRequestException(String.format("L'utilisateur %s existe déjà.", userCreationDTO.getUsername()));
        } else {
            final User request = new User(userCreationDTO);

            if (userCreationDTO.getPassword().equals(userCreationDTO.getPasswordConfirmation())) {
                request.setPassword(userCreationDTO.getPassword());
                return getMapper().toDto(getRepository().save(request));
            } else {
                throw new ApiBadRequestException("Les mots de passe ne correspondent pas.");
            }
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
    public void beforeSavingEntity(UserDTO request, User entity) {
        if (request instanceof final UserSecretsDTO secretsDTO) {

            if (Strings.isNotBlank(secretsDTO.getPassword())) {
                entity.setPassword(secretsDTO.getPassword());
                tokenService.invalidTokens(request.getId());
            }

        }
    }

    public void checkWhitelist(final String ip, final String username) {
        if (username.equalsIgnoreCase("api") && !ipUtils.canAccess(ip)) {
            throw new ApiForbiddenException("Vous n'êtes pas whitelisté.");
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

    private void sendApiUserMail() {
        final Optional<User> search = this.getRepository().findByUsername("api");

        if (search.isEmpty()) {
            final PasswordGenerator passwordGenerator = new PasswordGenerator();
            passwordGenerator.setAlphaDown(apiConfig.getPasswordMin());
            passwordGenerator.setAlphaUpper(apiConfig.getPasswordCaps());
            passwordGenerator.setNumbersAmount(apiConfig.getPasswordNumbers());
            passwordGenerator.setSpecialCharsAmount(apiConfig.getPasswordSpecials());

            User user = new User();
            user.setUsername("api");
            user.setPassword(passwordGenerator.generateRandomPassword());
            user.setRole(UserRole.ADMIN);
            user.setEmail(apiConfig.getEmail());
            user = super.getRepository().save(user);

            final FunixMailDTO mailDTO = new FunixMailDTO();
            mailDTO.setFrom("admin@funixgaming.fr");
            mailDTO.setTo(apiConfig.getEmail());
            mailDTO.setSubject("[FunixAPI] Identifiants pour le login api.");
            mailDTO.setText(String.format("username: %s password: %s", user.getUsername(), user.getPassword()));

            mailService.addMail(mailDTO);
            log.info("Envoi de la requête pour récupérer les infos api par mail.");
        }
    }
}
