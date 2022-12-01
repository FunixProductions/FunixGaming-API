package fr.funixgaming.api.core.crud.resources;

import fr.funixgaming.api.core.crud.clients.CrudClient;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import fr.funixgaming.api.core.crud.dtos.PageDTO;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class ApiResource<DTO extends ApiDTO, SERVICE extends CrudClient<DTO>> implements CrudClient<DTO> {

    private final SERVICE service;

    @Override
    public PageDTO<DTO> getAll(String page, String elemsPerPage, String search, String sort) {
        return service.getAll(page, elemsPerPage, search, sort);
    }

    @Override
    public DTO findById(String id) {
        final DTO dto = service.findById(id);

        if (dto != null) {
            return dto;
        } else {
            throw new ApiNotFoundException(String.format("The object id: %s does not exists.", id));
        }
    }

    @Override
    public DTO create(DTO request) {
        return service.create(request);
    }

    @Override
    public List<DTO> create(List<@Valid DTO> request) {
        return service.create(request);
    }

    @Override
    public DTO update(DTO request) {
        return service.update(request);
    }

    @Override
    public List<DTO> update(List<DTO> request) {
        return service.update(request);
    }

    @Override
    public void delete(String id) {
        service.delete(id);
    }

    @Override
    public void delete(String... ids) {
        service.delete(ids);
    }
}
