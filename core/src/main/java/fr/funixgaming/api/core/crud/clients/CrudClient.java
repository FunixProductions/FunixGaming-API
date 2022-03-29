package fr.funixgaming.api.core.crud.clients;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @FeignClient(name = "name", url = "${app.domain.url}/url")
 * @param <DTO> dto
 */
public interface CrudClient<DTO extends ApiDTO> {
    @GetMapping
    Set<DTO> getAll();

    @GetMapping("{id}")
    DTO findById(@PathVariable("id") String id);

    @PostMapping
    DTO create(@RequestBody DTO request);

    @PatchMapping
    DTO update(@RequestBody DTO request);

    @DeleteMapping
    void delete(@RequestParam String id);
}
