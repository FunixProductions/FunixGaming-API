package fr.funixgaming.api.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "funix.api")
public class FunixApiConfig {
    /**
     * Domain url to get the funix api<br>
     * Example : https://api.funixgaming.fr/
     */
    private String appDomainUrl;

    /**
     * Auth username used to access the funix api
     */
    private String userApiUsername;

    /**
     * Auth password used to access the funix api
     */
    private String userApiPassword;

    /**
     * Email of the owner of the app
     */
    private String email;

    /**
     * PasswordGenerator amount of numbers
     */
    private Integer passwordNumbers = 2;

    /**
     * PasswordGenerator amount of spacial chars
     */
    private Integer passwordSpecials = 2;

    /**
     * PasswordGenerator amount of high cases
     */
    private Integer passwordCaps = 5;

    /**
     * PasswordGenerator amount of mini cases
     */
    private Integer passwordMin = 5;

}
