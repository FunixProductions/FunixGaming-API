package fr.funixgaming.api.core.resources;

import fr.funixgaming.api.core.dtos.TestDTO;
import fr.funixgaming.api.core.services.TestService;

public class TestResource extends ApiResource<TestDTO, TestService> {
    public TestResource(TestService service) {
        super(service);
    }
}
