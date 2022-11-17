package fr.funixgaming.api.server.external_api_impl.twitch.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Token dto app token
 * <a href="https://dev.twitch.tv/docs/authentication/getting-tokens-oauth#client-credentials-grant-flow">Documentation</a>
 */
@Getter
@Setter
public class TwitchServerTokenDTO {

    /**
     * Access token twitch app server to server
     */
    @JsonProperty(value = "access_token")
    private String accessToken;

    /**
     * Time in seconds when the token will expire
     */
    @JsonProperty(value = "expires_in")
    private Integer expiresIn;

    /**
     * Token type, bearer
     */
    @JsonProperty(value = "token_type")
    private String tokenType;

}
