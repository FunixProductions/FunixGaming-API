package fr.funixgaming.api.core.doc;

import fr.funixgaming.api.core.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class Test extends ApiEntity {
    private String data;
}
