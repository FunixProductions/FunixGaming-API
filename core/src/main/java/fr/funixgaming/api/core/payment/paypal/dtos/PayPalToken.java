package fr.funixgaming.api.core.payment.paypal.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class PayPalToken {
    private String scope;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("app_id")
    private String appId;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    private String nonce;

    private Date generatedAt = Date.from(Instant.now());
}
