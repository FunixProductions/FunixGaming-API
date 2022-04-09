package fr.funixgaming.api.client.funixbot.dtos.user;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FunixBotUserDTO extends ApiDTO {
    @NotBlank
    private String userId;

    @NotBlank
    private String username;

    @NotNull
    private FunixBotUserExperienceDTO userExperience;

    @NotNull
    private Long lastMessageDateSeconds;
}
