package fr.funixgaming.api.core.doc;

import fr.funixgaming.api.core.dtos.ApiDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestDTO extends ApiDTO {
    private String data;
}
