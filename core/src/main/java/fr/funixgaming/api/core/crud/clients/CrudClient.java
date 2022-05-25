package fr.funixgaming.api.core.crud.clients;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * FeignClient(name = "name", url = "${funix.api.app-domain-url}", path = "/path")
 * @param <DTO> dto
 */
public interface CrudClient<DTO extends ApiDTO> {
    @GetMapping
    List<DTO> getAll(@RequestParam(value = "page", defaultValue = "0") String page,
                     @RequestParam(value = "elemsPerPage", defaultValue = "300") String elemsPerPage);

    @GetMapping("{id}")
    DTO findById(@PathVariable("id") String id);

    @GetMapping("search")
    List<DTO> search(@RequestParam(value = "q", defaultValue = "") String search,
                     @RequestParam(value = "page", defaultValue = "0") String page,
                     @RequestParam(value = "elemsPerPage", defaultValue = "300") String elemsPerPage);

    @PostMapping
    DTO create(@RequestBody @Valid DTO request);

    @PatchMapping
    DTO update(@RequestBody DTO request);

    @PatchMapping("batch")
    List<DTO> update(@RequestBody List<DTO> request);

    @DeleteMapping
    void delete(@RequestParam String id);

    @DeleteMapping("batch")
    void delete(@RequestParam String... ids);
}
