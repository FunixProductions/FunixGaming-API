package fr.funixgaming.api.server.user.components;

import fr.funixgaming.api.core.utils.network.IPUtils;
import fr.funixgaming.api.server.configs.FunixApiConfig;
import fr.funixgaming.api.server.user.services.UserTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserTokenService tokenService;
    private final FunixApiConfig apiConfig;
    private final IPUtils ipUtils;

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain chain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Strings.isEmpty(header) || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        final String clientIp = ipUtils.getClientIp(request);
        final UsernamePasswordAuthenticationToken authentication;

        if (!apiConfig.isIgnoreApiAccessAdmin() && ipUtils.isLocalClient(clientIp)) {
            authentication = tokenService.authenticateApiWhitelist();
        } else {
            final String token = header.split(" ")[1].trim();

            if (!tokenService.isTokenValid(token)) {
                chain.doFilter(request, response);
                return;
            }
            authentication = tokenService.authenticateToken(token);
        }

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

}
