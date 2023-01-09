package fr.funixgaming.api.server.external_api_impl.twitch.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchValidationTokenResponseDTO {

    @JsonProperty(value = "login")
    @SerializedName(value = "login")
    private String twitchUsername;

    @JsonProperty(value = "user_id")
    @SerializedName(value = "user_id")
    private String twitchUserId;

}
