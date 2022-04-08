package fr.funixgaming.api.client.funixbot.dtos.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FunixBotUserExperienceDTO extends FunixBotSubDTO {
    @NotNull
    private Integer xp;

    @NotNull
    private Integer xpNextLevel;

    @NotNull
    private Integer level;
}
