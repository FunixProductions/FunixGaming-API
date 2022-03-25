package fr.funixgaming.api.core.resource;

import fr.funixgaming.api.core.clients.CrudClient;
import fr.funixgaming.api.core.dtos.ApiDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public abstract class ApiResource<DTO extends ApiDTO, SERVICE extends CrudClient<DTO>> implements CrudClient<DTO> {

    private final SERVICE service;

    @Override
    public Set<DTO> getAll() {
        return service.getAll();
    }

    @Override
    public DTO getById(String id) {
        return service.getById(id);
    }

    @Override
    public DTO create(DTO request) {
        return service.create(request);
    }

    @Override
    public DTO update(DTO request) {
        return service.update(request);
    }

    @Override
    public void delete(String id) {
        service.delete(id);
    }
}
