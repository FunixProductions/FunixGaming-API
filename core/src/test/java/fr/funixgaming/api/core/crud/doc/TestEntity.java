package fr.funixgaming.api.core.crud.doc;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TestEntity extends ApiEntity {
    private String data;
    private Integer number;
}
