package fr.funixgaming.api.core.mail.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public abstract class ApiMailDTO {
    @Email
    @NotBlank
    private String to;

    @Email
    @NotBlank
    private String from;

    @NotBlank
    private String subject;

    @NotBlank
    private String text;
}
