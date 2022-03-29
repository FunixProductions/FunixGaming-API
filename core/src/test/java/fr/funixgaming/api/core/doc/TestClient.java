package fr.funixgaming.api.core.doc;

import fr.funixgaming.api.core.crud.clients.CrudClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("test")
public interface TestClient extends CrudClient<TestDTO> {
}
