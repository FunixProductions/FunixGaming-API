package fr.funixgaming.api.core.doc;

import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends ApiRepository<TestEntity> {
}
