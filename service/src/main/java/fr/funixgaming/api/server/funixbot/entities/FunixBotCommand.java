package fr.funixgaming.api.server.funixbot.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "funixbot_commands")
public class FunixBotCommand extends ApiEntity {
    @Column(nullable = false, unique = true, length = 30)
    private String command;

    @Column(nullable = false)
    private String message;
}
