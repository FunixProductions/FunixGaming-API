package fr.funixgaming.api.funixbot.client.dtos;

import com.funixproductions.core.crud.dtos.ApiDTO;
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
