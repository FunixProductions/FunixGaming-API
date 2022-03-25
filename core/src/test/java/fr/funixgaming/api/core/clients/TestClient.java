package fr.funixgaming.api.core.clients;

import fr.funixgaming.api.core.CoreAppTest;
import fr.funixgaming.api.core.dtos.TestDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(CoreAppTest.ROUTE)
public interface TestClient extends CrudClient<TestDTO> {
}
