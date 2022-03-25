package fr.funixgaming.api.core.resources;

import fr.funixgaming.api.core.clients.CrudClient;
import fr.funixgaming.api.core.dtos.ApiDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Getter
@RequiredArgsConstructor
@RestController
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
