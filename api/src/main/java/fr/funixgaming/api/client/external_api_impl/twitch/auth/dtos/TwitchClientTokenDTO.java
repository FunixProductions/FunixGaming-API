package fr.funixgaming.api.client.external_api_impl.twitch.auth.dtos;

import fr.funixgaming.api.client.user.dtos.UserDTO;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TwitchClientTokenDTO extends ApiDTO {
    private UserDTO user;

    private String twitchUserId;

    private String twitchUsername;

    private String accessToken;

    private Date expirationDateToken;
}
