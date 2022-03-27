package fr.funixgaming.api.client.user.dtos;

import fr.funixgaming.api.client.user.enums.UserRole;
import fr.funixgaming.api.core.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends ApiDTO {
    private String username;
    private String email;
    private UserRole role;
}
