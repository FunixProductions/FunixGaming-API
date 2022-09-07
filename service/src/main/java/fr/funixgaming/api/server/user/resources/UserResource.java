package fr.funixgaming.api.server.user.resources;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import fr.funixgaming.api.client.user.clients.UserCrudClient;
import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.client.user.dtos.UserTokenDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserCreationDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserLoginDTO;
import fr.funixgaming.api.client.user.dtos.requests.UserSecretsDTO;
import fr.funixgaming.api.core.crud.dtos.PageDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiForbiddenException;
import fr.funixgaming.api.core.google.services.GoogleCaptchaService;
import fr.funixgaming.api.core.utils.network.IPUtils;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.services.UserService;
import fr.funixgaming.api.server.user.services.UserTokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserResource implements UserCrudClient {
    private static final int MAX_ATTEMPT = 8;

    private final AuthenticationManager authenticationManager;
    private final GoogleCaptchaService captchaService;
    private final UserService userService;
    private final UserTokenService tokenService;
    private final IPUtils ipUtils;

    private final LoadingCache<String, Integer> triesCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES).build(new CacheLoader<>() {
                @Override
                @NonNull
                public Integer load(@NonNull String s) {
                    return 0;
                }
            });

    @PostMapping("register")
    public UserDTO register(@RequestBody @Valid UserCreationDTO request, final HttpServletRequest servletRequest) {
        userService.checkWhitelist(ipUtils.getClientIp(servletRequest), request.getUsername());

        if (!request.getUsername().equalsIgnoreCase("api")) {
            captchaService.checkCode(servletRequest);
        }

        return this.userService.register(request);
    }

    @PostMapping("login")
    public UserTokenDTO login(@RequestBody @Valid UserLoginDTO request, final HttpServletRequest servletRequest) {
        userService.checkWhitelist(ipUtils.getClientIp(servletRequest), request.getUsername());
        if (!request.getUsername().equalsIgnoreCase("api")) {
            captchaService.checkCode(servletRequest);
        }

        if (isBlocked(servletRequest)) {
            throw new ApiForbiddenException("Vous avez fait plus de 5 essais pour vous connecter. Veuillez patienter une heure.");
        }

        try {
            final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            final Authentication authenticate = authenticationManager.authenticate(auth);
            final User user = (User) authenticate.getPrincipal();

            triesCache.invalidate(ipUtils.getClientIp(servletRequest));
            return tokenService.generateAccessToken(user);
        } catch (BadCredentialsException ex) {
            failLogin(servletRequest);
            throw new ApiBadRequestException("Vos identifiants sont incorrects.", ex);
        }
    }

    /**
     * Route used to check usage of token.
     */
    @GetMapping("valid")
    public void valid() {
    }

    @GetMapping("current")
    public UserDTO currentUser() {
        final UserDTO userDTO = this.userService.getCurrentUser();

        if (userDTO == null) {
            throw new ApiForbiddenException("Vous n'êtes pas connecté.");
        } else {
            return userDTO;
        }
    }

    @GetMapping("{id}")
    public UserDTO findById(@PathVariable("id") String id) {
        return userService.findById(id);
    }

    @Override
    public PageDTO<UserDTO> getAll(String page, String elemsPerPage, String search, String sort) {
        return userService.getAll(page, elemsPerPage, search, sort);
    }

    @Override
    public UserDTO create(UserSecretsDTO request) {
        return userService.create(request);
    }

    @Override
    public UserDTO update(UserSecretsDTO request) {
        return userService.update(request);
    }

    @Override
    public void delete(String id) {
        userService.delete(id);
    }

    private void failLogin(final HttpServletRequest servletRequest) {
        final String key = ipUtils.getClientIp(servletRequest);

        int attempts = triesCache.getUnchecked(key);
        triesCache.put(key, attempts + 1);
    }

    private boolean isBlocked(final HttpServletRequest servletRequest) {
        return triesCache.getUnchecked(ipUtils.getClientIp(servletRequest)) >= MAX_ATTEMPT;
    }
}
