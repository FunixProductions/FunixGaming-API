package fr.funixgaming.api.core.crud.resources;

import fr.funixgaming.api.core.crud.clients.CrudClient;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
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
    public DTO findById(String id) {
        final DTO dto = service.findById(id);

        if (dto != null) {
            return dto;
        } else {
            throw new ApiNotFoundException(String.format("The entity id: %s is not found.", id));
        }
    }

    @Override
    public DTO create(DTO request) {
        return service.create(request);
    }

    @Override
    public DTO update(DTO request) {
        if (request.getId() == null) {
            throw new ApiBadRequestException("You need to specify the id of the object you want to update. Missing var id=uuid.");
        }

        final DTO dto = service.update(request);
        if (dto == null) {
            throw new ApiNotFoundException(String.format("The object id: %s does not exists.", request.getId()));
        } else {
            return dto;
        }
    }

    @Override
    public void delete(String id) {
        service.delete(id);
    }
}
