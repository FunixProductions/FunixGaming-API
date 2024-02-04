package fr.funixgaming.api.funixbot.client.dtos;

import com.funixproductions.core.crud.dtos.ApiDTO;
import fr.funixgaming.api.funixbot.client.enums.FunixBotCommandType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FunixBotCommandDTO extends ApiDTO {
    @NotBlank(message = "La commande ne peut pas être vide.")
    @Size(max = 30, message = "La commande ne peut pas dépasser 30 caractères.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "La commande ne peut contenir que des caractères alphanumériques.")
    private String command;

    @NotNull(message = "Le type de commande ne peut pas être vide.")
    private FunixBotCommandType type;

    @NotBlank(message = "Le message ne peut pas être vide.")
    @Size(max = 500, message = "Le message ne peut pas dépasser 500 caractères.")
    private String message;
}
