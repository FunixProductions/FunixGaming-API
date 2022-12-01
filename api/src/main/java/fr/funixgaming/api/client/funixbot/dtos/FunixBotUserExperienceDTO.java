package fr.funixgaming.api.client.funixbot.dtos;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FunixBotUserExperienceDTO extends ApiDTO {
    @NotBlank
    private String twitchUserId;

    @NotNull
    private Integer xp;

    @NotNull
    private Integer xpNextLevel;

    @NotNull
    private Integer level;

    @NotNull
    private Long lastMessageDateSeconds;
}
