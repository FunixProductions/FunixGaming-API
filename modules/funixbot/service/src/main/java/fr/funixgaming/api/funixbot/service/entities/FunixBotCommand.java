package fr.funixgaming.api.funixbot.service.entities;

import com.funixproductions.core.crud.entities.ApiEntity;
import fr.funixgaming.api.funixbot.client.enums.FunixBotCommandType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "funixbot_commands")
public class FunixBotCommand extends ApiEntity {
    @Column(nullable = false, unique = true, length = 30)
    private String command;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(200) DEFAULT 'OTHER'")
    private FunixBotCommandType type;

    @Column(nullable = false, length = 500)
    private String message;
}
