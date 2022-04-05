package fr.funixgaming.api.client.user.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserAdminDTO extends UserDTO {
    @NotBlank
    private String password;
}
