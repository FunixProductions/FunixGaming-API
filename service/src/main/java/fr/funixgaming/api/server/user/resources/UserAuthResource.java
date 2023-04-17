package fr.funixgaming.api.server.user.resources;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.external.google.captcha.services.GoogleCaptchaService;
import fr.funixgaming.api.server.user.services.CurrentSession;
import fr.funixgaming.api.server.user.services.UserAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/auth")
@RequiredArgsConstructor
public class UserAuthResource {

    private final UserAuthService userAuthService;
    private final CurrentSession currentSession;

    private final GoogleCaptchaService captchaService;
    private static final String CAPTCHA_REGISTER = "register";
    private static final String CAPTCHA_LOGIN = "login";

    @PostMapping("register")
    public UserDTO register(@RequestBody @Valid UserCreationDTO request, final HttpServletRequest servletRequest) {
        captchaService.checkCode(servletRequest, CAPTCHA_REGISTER);

        return userAuthService.register(request);
    }

    @PostMapping("login")
    public UserTokenDTO login(@RequestBody @Valid UserLoginDTO request, final HttpServletRequest servletRequest) {
        captchaService.checkCode(servletRequest, CAPTCHA_LOGIN);

        return userAuthService.login(request, servletRequest);
    }

    @GetMapping("current")
    public UserDTO currentUser() {
        final UserDTO userDTO = currentSession.getCurrentUser();

        if (userDTO == null) {
            throw new ApiForbiddenException("Vous n'êtes pas connecté.");
        } else {
            return userDTO;
        }
    }
}
