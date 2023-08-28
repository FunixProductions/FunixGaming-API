package fr.funixgaming.api.funixbot.client.dtos;

import com.funixproductions.core.crud.dtos.ApiDTO;
import fr.funixgaming.api.funixbot.client.enums.FunixBotCommandType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FunixBotCommandDTO extends ApiDTO {
    @NotBlank
    private String command;

    @NotNull
    private FunixBotCommandType type;

    @NotBlank
    private String message;
}
