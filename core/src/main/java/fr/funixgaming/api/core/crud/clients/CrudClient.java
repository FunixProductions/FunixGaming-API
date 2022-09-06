package fr.funixgaming.api.core.crud.clients;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * FeignClient(name = "name", url = "${funix.api.app-domain-url}", path = "/path")
 * @param <DTO> dto
 */
public interface CrudClient<DTO extends ApiDTO> {

    /**
     * @param page page Pagination number, starts with 0
     * @param elemsPerPage elemsPerPage Number of elements per page
     * @param search search Query for search. If empty ignored, empty by default
     *               Example : ?search=field1:operation1:value1,field2:operation2:value2
     *               Real example : ?search=firstName:like:john,lastName:like:doe
     *               Format for date search : dd-MM-yyyy_HH.mm.ss
     *               Operation can be
     *               - equals (equal)
     *               - not_equals (not equal)
     *               - greater_than (greater than)
     *               - GREATER_THAN_EQUAL (greater than or equal)
     *               - LESS_THAN (less than)
     *               - LESS_THAN_EQUAL (less than or equal)
     *               - LIKE (like)
     * @param sort sort Query for sorting. If empty ignored, empty by default
     *             Example : ?sort=field1:direction1,field2:direction2
     *             Direction can be
     *             - asc (ascending)
     *             - desc (descending)
     * @return Page<DTO>
     */
    @GetMapping
    Page<DTO> getAll(@RequestParam(value = "page", defaultValue = "0") String page,
                     @RequestParam(value = "elemsPerPage", defaultValue = "300") String elemsPerPage,
                     @RequestParam(value = "search", defaultValue = "") String search,
                     @RequestParam(value = "sort", defaultValue = "") String sort);

    @GetMapping("{id}")
    DTO findById(@PathVariable("id") String id);

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
