package fr.funixgaming.api.core.entities;

import org.hibernate.annotations.NaturalId;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.UUID;

@MappedSuperclass
public abstract class ApiEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @NaturalId
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private String uuid;

    @PrePersist
    @PreUpdate
    public void onCreateOrUpdate() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }

    public UUID getUuid() {
        if (uuid == null) {
            return null;
        } else {
            return UUID.fromString(uuid);
        }
    }

    public void setUuid(final @Nullable UUID uuid) {
        if (uuid == null) {
            this.uuid = null;
        } else {
            this.uuid = uuid.toString();
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
