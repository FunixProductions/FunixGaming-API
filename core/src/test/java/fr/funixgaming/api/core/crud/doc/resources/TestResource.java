package fr.funixgaming.api.core.crud.doc.resources;

import fr.funixgaming.api.core.crud.doc.clients.TestClient;
import fr.funixgaming.api.core.crud.doc.dtos.TestDTO;
import fr.funixgaming.api.core.crud.doc.services.TestService;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestResource extends ApiResource<TestDTO, TestService> implements TestClient {
    public TestResource(TestService service) {
        super(service);
    }
}
