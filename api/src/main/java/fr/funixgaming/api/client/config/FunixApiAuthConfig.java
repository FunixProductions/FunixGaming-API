package fr.funixgaming.api.client.config;

import fr.funixgaming.api.client.user.clients.UserAuthClient;
import org.springframework.stereotype.Component;

@Component
public class FunixApiAuthConfig {

    private final FunixApiConfig funixApiConfig;
    private final UserAuthClient userAuthClient;

    public FunixApiAuthConfig(FunixApiConfig funixApiConfig,
                              UserAuthClient userAuthClient) {
        this.funixApiConfig = funixApiConfig;
        this.userAuthClient = userAuthClient;
    }

}
