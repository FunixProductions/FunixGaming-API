package fr.funixgaming.api.client.user.dtos;

import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends ApiDTO {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private UserRole role;
}
