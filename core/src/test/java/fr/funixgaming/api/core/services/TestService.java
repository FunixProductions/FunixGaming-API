package fr.funixgaming.api.core.services;

import fr.funixgaming.api.core.dtos.TestDTO;
import fr.funixgaming.api.core.entities.Test;
import fr.funixgaming.api.core.mappers.TestMapper;
import fr.funixgaming.api.core.repositories.TestRepository;
import org.springframework.stereotype.Service;

@Service
public class TestService extends ApiService<TestDTO, Test, TestMapper, TestRepository> {
    public TestService(TestRepository repository, TestMapper mapper) {
        super(repository, mapper);
    }
}
