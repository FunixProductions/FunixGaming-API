package fr.funixgaming.api.server.user.components;

import fr.funixgaming.api.core.utils.network.IPUtils;
import fr.funixgaming.api.server.user.entities.User;
import fr.funixgaming.api.server.user.services.UserService;
import fr.funixgaming.api.server.user.services.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final UserTokenService tokenService;
    private final IPUtils ipUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Strings.isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();
        if (!tokenService.isTokenValid(token)) {
            chain.doFilter(request, response);
            return;
        }

        final UsernamePasswordAuthenticationToken authentication = tokenService.authenticateToken(token);
        final Object principal = authentication.getPrincipal();

        if (principal instanceof final User user) {
            userService.checkWhitelist(ipUtils.getClientIp(request), user.getUsername());
        }

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

}
