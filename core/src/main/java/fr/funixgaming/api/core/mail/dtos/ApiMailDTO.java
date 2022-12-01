package fr.funixgaming.api.core.mail.dtos;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ApiMailDTO extends ApiDTO {
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
