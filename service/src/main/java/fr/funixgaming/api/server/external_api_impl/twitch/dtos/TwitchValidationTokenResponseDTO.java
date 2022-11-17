package fr.funixgaming.api.server.external_api_impl.twitch.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchValidationTokenResponseDTO {

    @JsonProperty(value = "login")
    private String twitchUsername;

    @JsonProperty(value = "user_id")
    private String twitchUserId;

}
