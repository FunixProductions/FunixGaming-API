package fr.funixgaming.api.funixbot.service.entities;

import com.funixproductions.core.crud.entities.ApiEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "funixbot_automessages")
public class FunixBotAutoMessage extends ApiEntity {

    /**
     * The message to send.
     */
    @Column(nullable = false, length = 500)
    private String message;

    /**
     * The game name to send the message to.
     * Null if you can send it anytime
     */
    @Column(name = "game_name", length = 100)
    private String gameName;

    /**
     * If the message is announced or not in the twitch chat.
     */
    @Column(nullable = false, name = "is_announced", columnDefinition = "boolean default false")
    private Boolean isAnnounced = Boolean.FALSE;

}
