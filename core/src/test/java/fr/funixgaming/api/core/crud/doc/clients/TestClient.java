package fr.funixgaming.api.core.crud.doc.clients;

import fr.funixgaming.api.core.crud.clients.CrudClient;
import fr.funixgaming.api.core.crud.doc.dtos.TestDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("test")
public interface TestClient extends CrudClient<TestDTO> {
}
