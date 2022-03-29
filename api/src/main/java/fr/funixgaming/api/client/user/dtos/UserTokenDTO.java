package fr.funixgaming.api.client.user.dtos;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserTokenDTO extends ApiDTO {
    private UserDTO user;
    private String token;
    private Instant expirationDate;
}
