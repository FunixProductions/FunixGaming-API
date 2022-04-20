package fr.funixgaming.api.core.crud.clients;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * @FeignClient(name = "name", url = "${app.domain.url}", path = "/path")
 * @param <DTO> dto
 */
public interface CrudClient<DTO extends ApiDTO> {
    @GetMapping
    Set<DTO> getAll();

    @GetMapping("{id}")
    DTO findById(@PathVariable("id") String id);

    @PostMapping
    DTO create(@RequestBody @Valid DTO request);

    @PatchMapping
    DTO update(@RequestBody DTO request);

    @PatchMapping("batch")
    Set<DTO> update(@RequestBody Set<DTO> request);

    @DeleteMapping
    void delete(@RequestParam String id);
}
