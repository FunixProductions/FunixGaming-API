package fr.funixgaming.api.client.user.dtos;

import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
