package fr.funixgaming.api.core.crud.resources;

import fr.funixgaming.api.core.crud.clients.CrudClient;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class ApiResource<DTO extends ApiDTO, SERVICE extends CrudClient<DTO>> implements CrudClient<DTO> {

    private final SERVICE service;

    @Override
    public List<DTO> getAll(String page, String elemsPerPage) {
        return service.getAll(page, elemsPerPage);
    }

    @Override
    public DTO findById(String id) {
        final DTO dto = service.findById(id);

        if (dto != null) {
            return dto;
        } else {
            throw new ApiNotFoundException(String.format("The entity id: %s is not found.", id));
        }
    }

    @Override
    public List<DTO> search(String search, String page, String elemsPerPage) {
        return service.search(search, page, elemsPerPage);
    }

    @Override
    public DTO create(DTO request) {
        return service.create(request);
    }

    @Override
    public DTO update(DTO request) {
        final DTO dto = service.update(request);

        if (dto == null) {
            throw new ApiNotFoundException(String.format("The object id: %s does not exists.", request.getId()));
        } else {
            return dto;
        }
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
