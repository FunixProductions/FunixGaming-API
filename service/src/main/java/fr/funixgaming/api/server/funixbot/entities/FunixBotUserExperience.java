package fr.funixgaming.api.server.funixbot.entities;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Getter
@Setter
@Entity(name = "funixbot_users_experience")
public class FunixBotUserExperience extends ApiEntity {
    @Column(name = "user_id", updatable = false, unique = true)
    private String userId;

    private String username;

    private Integer xp;

    @Column(name = "xp_next_level")
    private Integer xpNextLevel;

    private Integer level;

    @Column(name = "last_message_date")
    private Date lastMessageDate;
}
