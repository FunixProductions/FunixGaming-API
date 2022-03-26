package fr.funixgaming.api.server.user.components;

import fr.funixgaming.api.server.configs.FunixApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final static String ISSUER = "FunixApi - api.funixgaming.fr";
    private final static Integer EXPIRATION_SECONDS_TOKEN = 604800; //1 Week

    private final FunixApiConfig funixApiConfig;


}
