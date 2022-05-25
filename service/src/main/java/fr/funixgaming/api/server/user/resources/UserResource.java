package fr.funixgaming.api.server.user.resources;

import fr.funixgaming.api.client.config.FunixApiConfig;
import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import fr.funixgaming.api.client.user.clients.UserCrudClient;
import fr.funixgaming.api.client.user.dtos.requests.UserAdminDTO;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiException;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.google.services.GoogleCaptchaService;
import fr.funixgaming.api.core.utils.network.IPUtils;
import fr.funixgaming.api.server.mail.services.FunixMailService;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserResource implements UserCrudClient {
    private final AuthenticationManager authenticationManager;
    private final GoogleCaptchaService captchaService;
    private final FunixMailService mailService;
    private final UserService userService;
    private final FunixApiConfig funixApiConfig;
    private final IPUtils ipUtils;

    @PostMapping("register")
    public UserDTO register(@RequestBody @Valid UserCreationDTO request, final HttpServletRequest servletRequest) {
        userService.checkWhitelist(ipUtils.getClientIp(servletRequest), request.getUsername());

        if (!request.getUsername().equalsIgnoreCase("api")) {
            captchaService.checkCode(servletRequest);
        }

        return this.userService.create(request);
    }

    @PostMapping("login")
    public UserTokenDTO login(@RequestBody @Valid UserLoginDTO request, final HttpServletRequest servletRequest) {
        userService.checkWhitelist(ipUtils.getClientIp(servletRequest), request.getUsername());
        if (!request.getUsername().equalsIgnoreCase("api")) {
            captchaService.checkCode(servletRequest);
        }

        try {
            final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            final Authentication authenticate = authenticationManager.authenticate(auth);
            final User user = (User) authenticate.getPrincipal();

            return userService.generateAccessToken(user);
        } catch (BadCredentialsException ex) {
            throw new ApiBadRequestException("Vos identifiants sont incorrects.", ex);
        }
    }

    /**
     * Route used to check usage of token.
     */
    @GetMapping("valid")
    public void valid() {
    }

    @GetMapping("{id}")
    public UserDTO findById(@PathVariable("id") String id) {
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

    @GetMapping("apiAccount")
    public String getApiInfos() {
        final User user = this.userService.getOrCreateApiUser();
        final FunixMailDTO mailDTO = new FunixMailDTO();

        mailDTO.setFrom("admin@funixgaming.fr");
        mailDTO.setTo(funixApiConfig.getEmail());
        mailDTO.setSubject("[FunixAPI] Identifiants pour le login api.");
        mailDTO.setText(String.format("username: %s password: %s", user.getUsername(), user.getPassword()));

        mailService.addMail(mailDTO);
        log.info("Envoi de la requête pour récupérer les infos api par mail.");

        return "Les infos api ont été envoyées par mail.";
    }

    @Override
    public List<UserDTO> getAll(String page,
                                String elemsPerPage) {
        return userService.getAll(page, elemsPerPage);
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
