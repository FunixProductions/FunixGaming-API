package fr.funixgaming.api.core.crud.services.search;

import fr.funixgaming.api.core.crud.entities.ApiEntity;
import fr.funixgaming.api.core.crud.enums.SearchOperation;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
        final SearchOperation operation = search.getOperation();

        switch (operation) {
            case EQUALS -> {
                return criteriaBuilder.equal(root.get(search.getKey()), search.getValue());
            }
            case NOT_EQUALS -> {
                return criteriaBuilder.notEqual(root.get(search.getKey()), search.getValue());
            }
            case GREATER_THAN -> {
                return criteriaBuilder.greaterThan(root.get(search.getKey()), search.getValue().toString());
            }
            case GREATER_THAN_OR_EQUAL_TO -> {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(search.getKey()), search.getValue().toString());
            }
            case LESS_THAN -> {
                return criteriaBuilder.lessThan(root.get(search.getKey()), search.getValue().toString());
            }
            case LESS_THAN_OR_EQUAL_TO -> {
                return criteriaBuilder.lessThanOrEqualTo(root.get(search.getKey()), search.getValue().toString());
            }
            case LIKE -> {
                return criteriaBuilder.like(root.get(search.getKey()), "%" + search.getValue() + "%");
            }
            case NOT_LIKE -> {
                return criteriaBuilder.notLike(root.get(search.getKey()), "%" + search.getValue() + "%");
            }
            case IS_NULL -> {
                return criteriaBuilder.isNull(root.get(search.getKey()));
            }
            case IS_NOT_NULL -> {
                return criteriaBuilder.isNotNull(root.get(search.getKey()));
            }
            case IS_TRUE -> {
                return criteriaBuilder.isTrue(root.get(search.getKey()));
            }
            case IS_FALSE -> {
                return criteriaBuilder.isFalse(root.get(search.getKey()));
            }
            default -> throw new ApiBadRequestException("Operation " + operation + " is not supported.");
        }
    }
}
