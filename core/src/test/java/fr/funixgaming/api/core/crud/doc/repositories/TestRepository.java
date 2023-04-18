package fr.funixgaming.api.core.crud.doc.repositories;

import fr.funixgaming.api.core.crud.doc.entities.TestEntity;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends ApiRepository<TestEntity> {
}
