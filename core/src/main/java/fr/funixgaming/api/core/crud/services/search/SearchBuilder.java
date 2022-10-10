package fr.funixgaming.api.core.crud.services.search;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import fr.funixgaming.api.core.crud.enums.SearchOperation;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

@Getter
public class SearchBuilder<ENTITY extends ApiEntity> {

    private Specification<ENTITY> specificationSearch = null;

    /**
     * Add search parameter to the list.
     * @param key The key of the search parameter. This is one of the fields of the entity.
     * @param operation Operation Key.
     * @param value Value of the search parameter. You can add [value1,value2] to search between two values.
     */
    public void with(final String key, final String operation, final String value) {
        SearchOperation searchOperation = null;

        for (final SearchOperation search : SearchOperation.values()) {
            if (search.getOperation().equalsIgnoreCase(operation)) {
                searchOperation = search;
                break;
            }
        }

        if (searchOperation == null) {
            throw new ApiBadRequestException("Votre recherche ne comporte pas la bonne opération. Utilisez un des enums de SearchOperation de la librairie FunixApi.");
        } else {
            if (value.contains("[") && value.contains("]")) {
                final String valuesNoBrackets = value.replace("[", "").replace("]", "");
                final String[] values = valuesNoBrackets.split("\\|");

                for (final String valueGet : values) {
                    addSearch(new Search(key, searchOperation, valueGet), true);
                }
            } else {
                addSearch(new Search(key, searchOperation, value), false);
            }
        }
    }

    private void addSearch(final Search search, boolean orPredicate) {
        final ApiSearch<ENTITY> apiSearch = new ApiSearch<>(search);

        if (specificationSearch == null) {
            specificationSearch = Specification.where(apiSearch);
        } else {
            if (orPredicate) {
                specificationSearch = specificationSearch.or(apiSearch);
            } else {
                specificationSearch = specificationSearch.and(apiSearch);
            }
        }
    }

}
