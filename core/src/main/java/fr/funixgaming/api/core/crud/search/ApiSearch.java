package fr.funixgaming.api.core.crud.search;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Getter
@RequiredArgsConstructor
public class ApiSearch<ENTITY extends ApiEntity> implements Specification<ENTITY> {

    private final Search search;

    @Override
    public Predicate toPredicate(@NonNull final Root<ENTITY> root,
                                 @NonNull final CriteriaQuery<?> query,
                                 @NonNull final CriteriaBuilder criteriaBuilder) {
        final String operation = search.getOperation();

        if (Strings.isEmpty(operation)) {
            throw new ApiBadRequestException("Votre recherche ne comporte pas le bon symbole de comparaison. Utilisez: > < >= <= ou =");
        } else if (Strings.isEmpty(search.getKey())) {
            throw new ApiBadRequestException("Vous n'avez pas spécifié de clé de recherche (nom du champ du DTO).");
        } else if (search.getValue() == null) {
            throw new ApiBadRequestException("Votre objet de comparaison de recherche est null.");
        }

        if (operation.equalsIgnoreCase(">")) {
            return criteriaBuilder.greaterThanOrEqualTo(root.get(search.getKey()), search.getValue().toString());
        } else if (operation.equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThanOrEqualTo(root.get(search.getKey()), search.getValue().toString());
        } else if (operation.equalsIgnoreCase("=")) {
            return criteriaBuilder.equal(root.get(search.getKey()), search.getValue());
        } else {
            throw new ApiBadRequestException("Votre recherche ne comporte pas le bon symbole de comparaison. Utilisez: > < >= <= ou =");
        }
    }
}
