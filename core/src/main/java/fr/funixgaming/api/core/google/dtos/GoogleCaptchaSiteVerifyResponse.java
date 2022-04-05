package fr.funixgaming.api.core.google.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleCaptchaSiteVerifyResponse {
    private boolean success;

    @JsonProperty("challenge_ts")
    private String executedAt;

    private String hostname;

    private float score;

    private String action;
}
