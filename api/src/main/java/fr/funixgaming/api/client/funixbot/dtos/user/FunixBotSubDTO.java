package fr.funixgaming.api.client.funixbot.dtos.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FunixBotSubDTO extends ApiDTO {
    @NotNull
    @JsonBackReference
    private transient FunixBotUserDTO user;
}
