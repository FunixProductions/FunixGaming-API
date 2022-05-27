package fr.funixgaming.api.core.crud.services;

import fr.funixgaming.api.core.crud.clients.CrudClient;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import fr.funixgaming.api.core.crud.search.SearchBuilder;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
public abstract class ApiService<DTO extends ApiDTO,
        ENTITY extends ApiEntity,
        MAPPER extends ApiMapper<ENTITY, DTO>,
        REPOSITORY extends ApiRepository<ENTITY>> implements CrudClient<DTO> {

    private final REPOSITORY repository;
    private final MAPPER mapper;

    @Override
    public List<DTO> getAll(String page, String elemsPerPage) {
        final int nbrPage;
        final int maxPerPage;

        try {
            if (Strings.isEmpty(page)) {
                nbrPage = 0;
            } else {
                nbrPage = Integer.parseInt(page);
            }

            if (Strings.isEmpty(elemsPerPage)) {
                maxPerPage = 300;
            } else {
                int max = Integer.parseInt(elemsPerPage);
                if (max > 300) {
                    max = 300;
                }
                maxPerPage = max;
            }
        } catch (NumberFormatException e) {
            throw new ApiBadRequestException("Vous avez rentré un nombre invalide.", e);
        }

        final Pageable pageable = PageRequest.of(nbrPage, maxPerPage);
        final List<DTO> toSend = new ArrayList<>();

        for (final ENTITY entity : repository.findAll(pageable)) {
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
    public List<DTO> search(String search, String page, String elemsPerPage) {
        final Page<ENTITY> data;
        final SearchBuilder searchBuilder = new SearchBuilder();
        final Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(elemsPerPage));

        if (!Strings.isEmpty(search)) {
            final Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            final Matcher matcher = pattern.matcher(search + ",");

            while (matcher.find()) {
                searchBuilder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }

            final Specification<ENTITY> specification = searchBuilder.build();
            data = repository.findAll(specification, pageable);
        } else {
            data = repository.findAll(pageable);
        }

        final List<DTO> result = new ArrayList<>();
        for (final ENTITY entity : data) {
            result.add(mapper.toDto(entity));
        }
        return result;
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
        return patch(request, getMapper(), getRepository());
    }

    @Override
    @Transactional
    public List<DTO> update(List<DTO> request) {
        final List<DTO> toSend = new ArrayList<>();

        for (final DTO data : request) {
            if (data.getId() != null) {
                toSend.add(patch(data, getMapper(), getRepository()));
            }
        }
        return toSend;
    }

    @Override
    @Transactional
    public void delete(String id) {
        final Optional<ENTITY> search = repository.findByUuid(id);

        if (search.isPresent()) {
            final ENTITY entity = search.get();
            repository.delete(entity);
        } else {
            throw new ApiNotFoundException("L'entité que vous voulez supprimer n'existe pas.");
        }
    }

    @Override
    @Transactional
    public void delete(String... ids) {
        final Set<String> idList = new HashSet<>(Arrays.asList(ids));
        final Iterable<ENTITY> search = this.repository.findAllByUuidIn(idList);
        repository.deleteAll(search);
    }

    @Nullable
    public static <DTO extends ApiDTO, ENTITY extends ApiEntity> DTO patch(@NonNull DTO request,
                                                                           ApiMapper<ENTITY, DTO> apiMapper,
                                                                           ApiRepository<ENTITY> apiRepository) {
        if (request.getId() == null) {
            throw new ApiBadRequestException("Pas d'id spécifié pour patch.");
        }

        final Optional<ENTITY> search = apiRepository.findByUuid(request.getId().toString());
        if (search.isPresent()) {
            ENTITY entity = search.get();
            final ENTITY entRequest = apiMapper.toEntity(request);

            entRequest.setId(null);
            entRequest.setUpdatedAt(Date.from(Instant.now()));
            apiMapper.patch(entRequest, entity);
            entity = apiRepository.save(entity);

            return apiMapper.toDto(entity);
        } else {
            return null;
        }
    }
}
