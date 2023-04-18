package fr.funixgaming.api.core.crud.doc.services;

import fr.funixgaming.api.core.crud.doc.dtos.TestDTO;
import fr.funixgaming.api.core.crud.doc.entities.TestEntity;
import fr.funixgaming.api.core.crud.doc.mappers.TestMapper;
import fr.funixgaming.api.core.crud.doc.repositories.TestRepository;
import fr.funixgaming.api.core.crud.services.ApiService;
import org.springframework.stereotype.Service;

@Service
public class TestService extends ApiService<TestDTO, TestEntity, TestMapper, TestRepository> {

    public TestService(TestRepository repository,
                       TestMapper mapper) {
        super(repository, mapper);
    }

}
