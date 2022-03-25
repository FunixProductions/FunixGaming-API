package fr.funixgaming.api.core.doc;

import fr.funixgaming.api.core.resources.ApiResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestResource extends ApiResource<TestDTO, TestService> implements TestClient {
    public TestResource(TestService service) {
        super(service);
    }
}
