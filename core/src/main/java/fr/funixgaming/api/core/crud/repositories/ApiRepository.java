package fr.funixgaming.api.core.crud.repositories;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.Set;

@NoRepositoryBean
public interface ApiRepository<ENTITY extends ApiEntity> extends JpaRepository<ENTITY, Long>, JpaSpecificationExecutor<ENTITY> {
    Optional<ENTITY> findByUuid(String uuid);
    Iterable<ENTITY> findAllByUuidIn(Iterable<String> uuidList);
}
