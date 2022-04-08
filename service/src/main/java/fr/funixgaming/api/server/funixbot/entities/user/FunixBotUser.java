package fr.funixgaming.api.server.funixbot.entities.user;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "funixbot_users")
public class FunixBotUser extends ApiEntity {
    @Column(name = "user_id", updatable = false, unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String username;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "experience_id", updatable = false, unique = true, nullable = false)
    private FunixBotUserExperience userExperience;

    @Column(name = "last_message_date", nullable = false)
    private Date lastMessageDate;
}
