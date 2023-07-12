package fr.funixgaming.api.funixbot.service.entities;

import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "funixbot_commands")
public class FunixBotCommand extends ApiEntity {
    @Column(nullable = false, unique = true, length = 30)
    private String command;

    @Column(nullable = false)
    private String message;
}
