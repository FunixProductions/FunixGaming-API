package fr.funixgaming.api.core.crud.doc;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestDTO extends ApiDTO {
    private String data;
    private Integer number;
}
