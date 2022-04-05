package fr.funixgaming.api.client.funixbot.dtos;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class FunixBotUserExperienceDTO extends ApiDTO {
    @NotBlank
    private String userId;

    @NotBlank
    private String username;

    @NotNull
    private Integer xp;

    @NotNull
    private Integer xpNextLevel;

    @NotNull
    private Integer level;

    @NotNull
    private Date lastMessageDate;
}