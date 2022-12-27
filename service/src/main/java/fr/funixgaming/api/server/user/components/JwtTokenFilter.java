package fr.funixgaming.api.server.user.components;

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

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain chain) throws ServletException, IOException {
        final String bearerTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Strings.isEmpty(bearerTokenHeader) || !bearerTokenHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        final String token = bearerTokenHeader.split(" ")[1].trim();
        final UsernamePasswordAuthenticationToken authentication = tokenService.isTokenValid(token);

        if (authentication == null) {
            chain.doFilter(request, response);
        } else {
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
    }

}
