package fr.funixgaming.api.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDTO implements ApiDTO {
    private UUID id;
    private String data;
}
