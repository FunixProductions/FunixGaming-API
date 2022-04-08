package fr.funixgaming.api.server.funixbot.entities.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "funixbot_users_experience")
public class FunixBotUserExperience extends FunixBotSubEntity {
    @Column(nullable = false)
    private Integer xp;

    @Column(name = "xp_next_level", nullable = false)
    private Integer xpNextLevel;

    @Column(nullable = false)
    private Integer level;
}
