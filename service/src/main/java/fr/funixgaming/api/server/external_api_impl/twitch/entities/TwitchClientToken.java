package fr.funixgaming.api.server.external_api_impl.twitch.entities;

import fr.funixgaming.api.client.external_api_impl.twitch.enums.TwitchClientTokenType;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import fr.funixgaming.api.server.configs.EncryptionString;
import fr.funixgaming.api.server.user.entities.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity(name = "twitch_client_tokens")
public class TwitchClientToken extends ApiEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Convert(converter = EncryptionString.class)
    @Column(name = "token_type", nullable = false)
    private TwitchClientTokenType tokenType;

    @Convert(converter = EncryptionString.class)
    @Column(name = "twitch_user_id", nullable = false, unique = true)
    private String twitchUserId;

    @Convert(converter = EncryptionString.class)
    @Column(name = "twitch_username", nullable = false, unique = true)
    private String twitchUsername;

    @Convert(converter = EncryptionString.class)
    @Column(name = "o_auth_code", nullable = false, unique = true)
    private String oAuthCode;

    @Convert(converter = EncryptionString.class)
    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;

    @Convert(converter = EncryptionString.class)
    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "expiration_date_token", nullable = false)
    private Date expirationDateToken;

    public boolean isUsable() {
        return Instant.now().isBefore(expirationDateToken.toInstant());
    }
}
