package fr.funixgaming.api.core.crud.services.search;

import fr.funixgaming.api.core.crud.enums.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Search {
    private String key;
    private SearchOperation operation;
    private String value;
}
