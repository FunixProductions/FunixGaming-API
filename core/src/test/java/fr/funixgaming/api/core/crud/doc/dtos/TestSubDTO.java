package fr.funixgaming.api.core.crud.doc.dtos;

import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestSubDTO extends ApiDTO {

    private TestDTO main;

    private String data;

}
