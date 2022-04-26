package fr.funixgaming.api.core.crud.search;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SearchBuilder {

    private final List<Search> params = new ArrayList<>();

    public SearchBuilder with(final String key, final String operation, final Object value) {
        params.add(new Search(key, operation, value));
        return this;
    }

    public <ENTITY extends ApiEntity> Specification<ENTITY> build() {
        if (params.isEmpty()) {
            throw new ApiBadRequestException("Vous n'avez pas spécifié de champs de recherche.");
        }

        final List<Specification<ENTITY>> specifications = new ArrayList<>();
        for (final Search search : params) {
            final ApiSearch<ENTITY> apiSearch = new ApiSearch<ENTITY>(search);
            specifications.add(apiSearch);
        }

        Specification<ENTITY> search = specifications.get(0);
        final int size = specifications.size();

        for (int i = 1; i < size; ++i) {
            search = Specification.where(search).and(specifications.get(i));
        }

        return search;
    }

}
