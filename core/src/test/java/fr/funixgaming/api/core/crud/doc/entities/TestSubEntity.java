package fr.funixgaming.api.core.crud.doc.entities;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TestSubEntity extends ApiEntity {

    @ManyToOne
    private TestEntity main;

    private String data;

}
