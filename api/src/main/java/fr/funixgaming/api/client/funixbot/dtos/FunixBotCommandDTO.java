package fr.funixgaming.api.client.funixbot.dtos;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FunixBotCommandDTO extends ApiDTO {
    @NotBlank
    private String command;

    @NotBlank
    private String message;
}
