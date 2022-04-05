package fr.funixgaming.api.core.google.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class GoogleCaptchaVerifyRequest {

    //The shared key between your site and reCAPTCHA.
    @NotBlank
    private String secret;

    //The user response token provided by the reCAPTCHA client-side integration on your site.
    @NotBlank
    private String response;

    //The user's IP address.
    @NotBlank
    private String remoteip;

}
