package fr.funixgaming.api.client.mail.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MailDTO {
    @NotBlank
    private String to;

    @NotBlank
    private String from;

    @NotBlank
    private String subject;

    @NotBlank
    private String text;
}
