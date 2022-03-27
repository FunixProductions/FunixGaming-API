package fr.funixgaming.api.core.services;

import fr.funixgaming.api.core.clients.CrudClient;
import fr.funixgaming.api.core.dtos.ApiDTO;
import fr.funixgaming.api.core.entities.ApiEntity;
import fr.funixgaming.api.core.mappers.ApiMapper;
import fr.funixgaming.api.core.repositories.ApiRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    public DTO findById(String id) {
        final Optional<ENTITY> entity = repository.findByUuid(id);

        if (entity.isPresent()) {
            return mapper.toDto(entity.get());
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public DTO create(DTO request) {
        final ENTITY entity = mapper.toEntity(request);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Nullable
    @Transactional
    public DTO update(DTO request) {
        final Optional<ENTITY> search = repository.findByUuid(request.getId().toString());

        if (search.isPresent()) {
            ENTITY entity = search.get();
            final ENTITY entRequest = mapper.toEntity(request);

            entRequest.setId(null);
            entRequest.setUpdatedAt(Date.from(Instant.now()));
            mapper.patch(entRequest, entity);
            entity = repository.save(entity);

            return mapper.toDto(entity);
        } else {
            return null;
        }
    }

    @Override
    public void delete(String id) {
        final Optional<ENTITY> search = repository.findByUuid(id);

        if (search.isPresent()) {
            final ENTITY entity = search.get();
            repository.delete(entity);
        }
    }
}
