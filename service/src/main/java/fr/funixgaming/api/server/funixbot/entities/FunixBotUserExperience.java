package fr.funixgaming.api.server.funixbot.entities;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "funixbot_users_experience")
public class FunixBotUserExperience extends ApiEntity {
    @Column(name = "twitch_user_id", nullable = false, updatable = false, unique = true)
    private String twitchUserId;

    @Column(nullable = false)
    private Integer xp;

    @Column(name = "xp_next_level", nullable = false)
    private Integer xpNextLevel;

    @Column(nullable = false)
    private Integer level;

    @Column(name = "last_message_date", nullable = false)
    private Date lastMessageDate;
}
