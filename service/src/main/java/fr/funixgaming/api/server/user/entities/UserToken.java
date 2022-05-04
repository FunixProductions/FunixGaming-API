package fr.funixgaming.api.server.user.entities;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import fr.funixgaming.api.server.converters.EncryptionString;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity(name = "api_users_tokens")
public class UserToken extends ApiEntity {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, updatable = false, length = 2000)
    @Convert(converter = EncryptionString.class)
    private String token;

    @Column(name = "expiration_date", updatable = false)
    private Date expirationDate;
}
