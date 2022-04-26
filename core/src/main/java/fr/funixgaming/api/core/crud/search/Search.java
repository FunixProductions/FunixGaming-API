package fr.funixgaming.api.core.crud.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Search {
    private String key;
    private String operation;
    private Object value;
}
