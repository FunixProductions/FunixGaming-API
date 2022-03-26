package fr.funixgaming.api.client.funixbot.dtos;

import fr.funixgaming.api.core.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FunixBotCommandDTO extends ApiDTO {
    private String command;
    private String message;
}
