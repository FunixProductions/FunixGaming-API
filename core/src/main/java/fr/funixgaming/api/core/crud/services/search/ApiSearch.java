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
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ApiSearch<ENTITY extends ApiEntity> implements Specification<ENTITY> {

    private transient final Search search;

    @Override
    public Predicate toPredicate(@NonNull final Root<ENTITY> root,
                                 @NonNull final CriteriaQuery<?> query,
                                 @NonNull final CriteriaBuilder criteriaBuilder) {
        try {
            final SearchOperation operation = search.getOperation();
            final Object valueSearch = castToRequiredType(root.get(search.getKey()).getJavaType(), search.getValue());

            switch (operation) {
                case EQUALS -> {
                    return criteriaBuilder.equal(root.get(search.getKey()), valueSearch);
                }
                case NOT_EQUALS -> {
                    return criteriaBuilder.notEqual(root.get(search.getKey()), valueSearch);
                }
                case GREATER_THAN -> {
                    return criteriaBuilder.greaterThan(root.get(search.getKey()), valueSearch.toString());
                }
                case GREATER_THAN_OR_EQUAL_TO -> {
                    return criteriaBuilder.greaterThanOrEqualTo(root.get(search.getKey()), valueSearch.toString());
                }
                case LESS_THAN -> {
                    return criteriaBuilder.lessThan(root.get(search.getKey()), valueSearch.toString());
                }
                case LESS_THAN_OR_EQUAL_TO -> {
                    return criteriaBuilder.lessThanOrEqualTo(root.get(search.getKey()), valueSearch.toString());
                }
                case LIKE -> {
                    return criteriaBuilder.like(root.get(search.getKey()), "%" + valueSearch + "%");
                }
                case NOT_LIKE -> {
                    return criteriaBuilder.notLike(root.get(search.getKey()), "%" + valueSearch + "%");
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
        } catch (IllegalArgumentException e) {
            throw new ApiBadRequestException("Le champ de recherche " + search.getKey() + " n'existe pas.", e);
        }
    }

    @NonNull
    private Object castToRequiredType(Class<?> fieldType, String value) {
        if (fieldType.isAssignableFrom(Double.class)) {
            return Double.valueOf(value);
        } else if (fieldType.isAssignableFrom(Integer.class)) {
            return Integer.valueOf(value);
        } else if (fieldType.isAssignableFrom(Long.class)) {
            return Long.parseLong(value);
        } else if (fieldType.isAssignableFrom(String.class)) {
            return value;
        } else if (fieldType.isEnum()) {
            return Enum.valueOf((Class<? extends Enum>) fieldType, value);
        } else if (fieldType.isAssignableFrom(Boolean.class)) {
            return Boolean.valueOf(value);
        } else if (fieldType.isAssignableFrom(UUID.class)) {
            return UUID.fromString(value);
        } else if (fieldType.isAssignableFrom(Float.class)) {
            return Float.valueOf(value);
        } else {
            throw new ApiBadRequestException("Type " + fieldType + " is not supported.");
        }
    }
}
