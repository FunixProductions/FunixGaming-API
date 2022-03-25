package fr.funixgaming.api.core.entities;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.UUID;

@Data
public abstract class ApiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @NaturalId
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @PrePersist
    @PreUpdate
    public void onCreateOrUpdate() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }
}
