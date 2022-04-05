package fr.funixgaming.api.core.crud.doc;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
public class TestEntity extends ApiEntity {
    private String data;
}
