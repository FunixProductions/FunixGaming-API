package fr.funixgaming.api.core.clients;

import fr.funixgaming.api.core.dtos.ApiDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

public interface CrudClient<DTO extends ApiDTO> {
    @GetMapping
    Set<DTO> getAll();

    @GetMapping()
    DTO getById(@RequestParam String id);

    @PostMapping
    DTO create(@RequestBody DTO request);

    @PatchMapping
    DTO update(@RequestBody DTO request);

    @DeleteMapping
    void delete(@PathVariable("id") String id);
}
