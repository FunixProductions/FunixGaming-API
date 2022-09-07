package fr.funixgaming.api.client.user.dtos.requests;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserSecretsDTO extends UserDTO {
    @NotBlank
    private String password;
}
