package fr.funixgaming.api.client.funixbot.dtos;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class FunixBotCommandDTO extends ApiDTO {
    @NotBlank
    private String command;

    @NotBlank
    private String message;
}
