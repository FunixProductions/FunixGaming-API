package fr.funixgaming.api.client.funixbot.dtos.user;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
