package fr.funixgaming.api.core.doc;

import fr.funixgaming.api.core.crud.services.ApiService;
import org.springframework.stereotype.Service;

@Service
public class TestService extends ApiService<TestDTO, TestEntity, TestMapper, TestRepository> {

    public TestService(TestRepository repository,
                       TestMapper mapper) {
        super(repository, mapper);
    }

}
