package fr.funixgaming.api.core.crud.repositories;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

@NoRepositoryBean
public interface ApiRepository<ENTITY extends ApiEntity> extends PagingAndSortingRepository<ENTITY, Long> {
    Optional<ENTITY> findByUuid(String uuid);
    Iterable<ENTITY> findAllByUuid(String uuid);
}
