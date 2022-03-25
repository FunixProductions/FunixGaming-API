package fr.funixgaming.api.client.funixbot.dtos;

import fr.funixgaming.api.core.dtos.ApiDTO;
import lombok.Data;

import java.util.UUID;

@Data
public class FunixBotCommandDTO implements ApiDTO {
    private final UUID id;
    private final String command;
    private final String message;
}
