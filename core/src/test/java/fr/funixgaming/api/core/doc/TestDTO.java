package fr.funixgaming.api.core.doc;

import fr.funixgaming.api.core.dtos.ApiDTO;
import lombok.Data;

import java.util.UUID;

@Data
public class TestDTO implements ApiDTO {
    private UUID id;
    private String data;
}
