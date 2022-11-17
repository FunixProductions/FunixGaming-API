package fr.funixgaming.api.server.external_api_impl.twitch.auth.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("twitch.api")
public class TwitchApiConfig {

    /**
     * Client id from twitch application
     */
    private String appClientId;

    /**
     * Secret from twitch application
     */
    private String appClientSecret;

    /**
     * Domain callback where the users are redirected
     */
    private String appCallback;

    /**
     * Domain twitch api url auth
     */
    private String appAuthDomainUrl;

}
