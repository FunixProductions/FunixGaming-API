package fr.funixgaming.api.core.crud.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class ApiEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @NaturalId
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private String uuid;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    public void onCreate() {
        updateUuid();
        createdAt = Date.from(Instant.now());
    }

    @PreUpdate
    public void onUpdate() {
        updateUuid();
        updatedAt = Date.from(Instant.now());
    }

    private void updateUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof final ApiEntity apiEntity) {
            if (this.id != null && apiEntity.getId() != null) {
                return apiEntity.getId().equals(this.id);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public UUID getUuid() {
        if (uuid == null) {
            return null;
        } else {
            return UUID.fromString(uuid);
        }
    }

    public void setUuid(final UUID uuid) {
        if (uuid == null) {
            this.uuid = null;
        } else {
            this.uuid = uuid.toString();
        }
    }
}
