package fr.funixgaming.api.core.repositories;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApiRepository<ENTITY> extends CrudRepository<ENTITY, Long> {
    Optional<ENTITY> findByUuid(String uuid);
    Iterable<ENTITY> findAllByUuid(Iterable<String> uuid);
}
