package fr.funixgaming.api.core.repositories;

import fr.funixgaming.api.core.entities.ApiEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ApiRepository<ENTITY extends ApiEntity> extends CrudRepository<ENTITY, Long> {
    Optional<ENTITY> findByUuid(String uuid);
    Iterable<ENTITY> findAllByUuid(String uuid);
}