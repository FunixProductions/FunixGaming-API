package fr.funixgaming.api.client.user.dtos.requests;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSecretsDTO extends UserDTO {
    @NotBlank
    private String password;
}
