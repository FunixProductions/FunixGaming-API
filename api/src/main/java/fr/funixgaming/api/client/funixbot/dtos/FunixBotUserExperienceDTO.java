package fr.funixgaming.api.client.funixbot.dtos;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
public class FunixBotUserExperienceDTO extends ApiDTO {
    @NotBlank
    private String userId;

    @NotBlank
    private String username;

    @NotBlank
    private Integer xp;

    @NotBlank
    private Integer xpNextLevel;

    @NotBlank
    private Integer level;

    @NotBlank
    private Date lastMessageDate;
}
