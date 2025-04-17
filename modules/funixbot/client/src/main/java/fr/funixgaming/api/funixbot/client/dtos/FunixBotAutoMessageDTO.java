package fr.funixgaming.api.funixbot.client.dtos;

import com.funixproductions.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FunixBotAutoMessageDTO extends ApiDTO {

    /**
     * The message to send.
     */
    @NotBlank(message = "Le message ne peut pas être vide.")
    @Size(max = 500, message = "Le message ne peut pas dépasser 500 caractères.")
    private String message;

    /**
     * The game name to send the message to.
     * Null if you can send it anytime
     */
    @Nullable
    @Size(max = 100, message = "Le nom du jeu ne peut pas dépasser 100 caractères.")
    private String gameName;

    /**
     * If the message is announced or not in the twitch chat.
     */
    @NotNull(message = "Le message doit être annoncé ou non.")
    private Boolean isAnnounced = Boolean.FALSE;

}
