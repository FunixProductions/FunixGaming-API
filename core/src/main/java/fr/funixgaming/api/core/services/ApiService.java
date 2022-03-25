package fr.funixgaming.api.core.services;

import fr.funixgaming.api.core.clients.CrudClient;
import fr.funixgaming.api.core.dtos.ApiDTO;
import fr.funixgaming.api.core.entities.ApiEntity;
import fr.funixgaming.api.core.mappers.ApiMapper;
import fr.funixgaming.api.core.repositories.ApiRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Getter
@RequiredArgsConstructor
public abstract class ApiService<DTO extends ApiDTO,
        ENTITY extends ApiEntity,
        MAPPER extends ApiMapper<ENTITY, DTO>,
        REPOSITORY extends ApiRepository<ENTITY>> implements CrudClient<DTO> {

    private final REPOSITORY repository;
    private final MAPPER mapper;

    @Override
    public Set<DTO> getAll() {
        final Set<DTO> toSend = new HashSet<>();

        for (final ENTITY entity : repository.findAll()) {
            toSend.add(mapper.toDto(entity));
        }
        return toSend;
    }

    @Override
    @Nullable
    public DTO getById(String id) {
        final Optional<ENTITY> search = repository.findByUuid(id);

        if (search.isPresent()) {
            final ENTITY entity = search.get();
            return mapper.toDto(entity);
        } else {
            return null;
        }
    }

    @Override
    public DTO create(DTO request) {
        final ENTITY entity = repository.save(mapper.toEntity(request));
        return mapper.toDto(entity);
    }

    @Override
    public DTO update(DTO request) {
        final Optional<ENTITY> entity = repository.findByUuid(request.getId().toString());
        return null;
    }

    @Override
    public void delete(String id) {

    }
}
