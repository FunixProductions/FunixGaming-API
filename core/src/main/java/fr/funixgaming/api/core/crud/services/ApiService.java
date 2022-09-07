package fr.funixgaming.api.core.crud.services;

import fr.funixgaming.api.core.crud.clients.CrudClient;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import fr.funixgaming.api.core.crud.entities.ApiEntity;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import fr.funixgaming.api.core.crud.services.search.SearchBuilder;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

@Getter
@RequiredArgsConstructor
public abstract class ApiService<DTO extends ApiDTO,
        ENTITY extends ApiEntity,
        MAPPER extends ApiMapper<ENTITY, DTO>,
        REPOSITORY extends ApiRepository<ENTITY>> implements CrudClient<DTO> {

    private final REPOSITORY repository;
    private final MAPPER mapper;

    @Override
    @Transactional
    public Page<DTO> getAll(@Nullable String page, @Nullable String elemsPerPage, @Nullable String search, @Nullable String sort) {
        final Specification<ENTITY> specificationSearch = getSpecification(search);
        final Pageable pageable = getPage(page, elemsPerPage, sort);
        final Page<DTO> toReturn = repository.findAll(specificationSearch, pageable).map(mapper::toDto);

        for (final DTO dto : toReturn) {
            beforeSendingDTO(dto, null);
        }
        return toReturn;
    }

    @Override
    @Nullable
    @Transactional
    public DTO findById(String id) {
        final Optional<ENTITY> search = repository.findByUuid(id);

        if (search.isPresent()) {
            final ENTITY entity = search.get();
            final DTO response = mapper.toDto(entity);

            beforeSendingDTO(response, entity);
            return response;
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    @Transactional
    public DTO create(DTO request) {
        ENTITY entity = mapper.toEntity(request);

        beforeSavingEntity(request, entity);
        entity = repository.save(entity);

        final DTO dto = mapper.toDto(repository.save(entity));
        beforeSendingDTO(dto, entity);
        return dto;
    }

    @NonNull
    @Override
    @Transactional
    public DTO update(DTO request) {
        if (request.getId() == null) {
            throw new ApiBadRequestException("Vous n'avez pas spécifié d'id.");
        }

        final Optional<ENTITY> search = repository.findByUuid(request.getId().toString());
        if (search.isPresent()) {
            ENTITY entity = search.get();
            final ENTITY entRequest = mapper.toEntity(request);

            entRequest.setId(null);
            entRequest.setUpdatedAt(Date.from(Instant.now()));
            mapper.patch(entRequest, entity);

            beforeSavingEntity(request, entity);
            entity = repository.save(entity);

            final DTO dto = mapper.toDto(entity);
            beforeSendingDTO(dto, entity);
            return dto;
        } else {
            throw new ApiNotFoundException(String.format("L'entité id %s n'existe pas.", request.getId()));
        }
    }

    @Override
    @Transactional
    public List<DTO> update(List<DTO> request) {
        final List<DTO> toSend = new ArrayList<>();

        for (final DTO data : request) {
            if (data.getId() != null) {
                toSend.add(this.update(data));
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

            beforeDeletingEntity(entity);
            repository.delete(entity);
        } else {
            throw new ApiNotFoundException(String.format("L'entité id %s n'existe pas.", id));
        }
    }

    @Override
    @Transactional
    public void delete(String... ids) {
        final Set<String> idList = new HashSet<>(Arrays.asList(ids));
        final Iterable<ENTITY> search = this.repository.findAllByUuidIn(idList);

        for (final ENTITY entity : search) {
            beforeDeletingEntity(entity);
        }
        repository.deleteAll(search);
    }

    /**
     * Method used when you need to add some logic before saving an entity.
     * Override it when you have specific logic to add.
     *
     * @param request request received.
     * @param entity entity fetched from database.
     */
    public void beforeSavingEntity(@NonNull DTO request, @NonNull ENTITY entity) {
    }

    /**
     * Method used to when you need to
     * add some logic before sending DTO to client.
     *
     * @param dto dto fetched from database.
     * @param entity entity fetched from database.
     */
    public void beforeSendingDTO(@NonNull DTO dto, @Nullable ENTITY entity) {
    }

    /**
     * Method used when you need to add some logic before removing an entity.
     * @param entity entity fetched from database.
     */
    public void beforeDeletingEntity(@NonNull ENTITY entity) {
    }

    private Pageable getPage(final String page, final String elemsPerPage, @Nullable final String sortQuery) {
        final Sort sort = getSort(sortQuery);
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
            throw new ApiBadRequestException("Vous avez entré un nombre invalide.", e);
        }

        return PageRequest.of(nbrPage, maxPerPage, sort);
    }

    private Sort getSort(@Nullable final String sortQuery) {
        if (Strings.isEmpty(sortQuery)) {
            return Sort.unsorted();
        } else {
            final String[] sort = sortQuery.split(",");
            final List<Sort.Order> orders = new ArrayList<>();

            for (final String s : sort) {
                final String[] sortElem = s.split(":");

                if (sortElem.length == 2) {
                    final String field = sortElem[0];
                    final String order = sortElem[1];

                    if (order.equalsIgnoreCase("asc")) {
                        orders.add(Sort.Order.asc(field));
                    } else if (order.equalsIgnoreCase("desc")) {
                        orders.add(Sort.Order.desc(field));
                    } else {
                        throw new ApiBadRequestException("Vous avez entré un ordre de tri invalide. (asc ou desc)");
                    }
                } else {
                    throw new ApiBadRequestException("Vous avez entré un ordre de tri invalide. (asc ou desc)");
                }
            }

            return Sort.by(orders);
        }
    }

    @Nullable
    private Specification<ENTITY> getSpecification(@Nullable final String search) {
        if (Strings.isEmpty(search)) {
            return null;
        }

        final SearchBuilder searchBuilder = new SearchBuilder();
        final String[] searchs = search.split(",");

        for (final String searchQuery : searchs) {
            final String[] searchSplit = searchQuery.split(":");

            if (searchSplit.length != 3) {
                throw new ApiBadRequestException("La recherche est invalide. Vous devez respecter le format suivant : key:operation:value");
            } else {
                final String key = searchSplit[0];
                final String operation = searchSplit[1];
                final String value = searchSplit[2];

                searchBuilder.with(key, operation, value);
            }
        }

        return searchBuilder.build();
    }

}
