package fr.funixgaming.api.core.external.google.captcha.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleCaptchaSiteVerifyResponse {
    private boolean success;

    private String hostname;

    private float score;

    private String action;
}
