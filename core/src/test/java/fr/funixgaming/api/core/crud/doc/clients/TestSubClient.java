package fr.funixgaming.api.core.crud.doc.clients;

import fr.funixgaming.api.core.crud.clients.CrudClient;
import fr.funixgaming.api.core.crud.doc.dtos.TestSubDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("TestSub")
public interface TestSubClient extends CrudClient<TestSubDTO> {
}
