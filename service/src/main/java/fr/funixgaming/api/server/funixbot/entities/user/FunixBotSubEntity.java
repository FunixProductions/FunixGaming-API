package fr.funixgaming.api.server.funixbot.entities.user;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Getter
@Setter
@MappedSuperclass
public abstract class FunixBotSubEntity extends ApiEntity {
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", updatable = false, unique = true, nullable = false)
    private FunixBotUser user;
}
