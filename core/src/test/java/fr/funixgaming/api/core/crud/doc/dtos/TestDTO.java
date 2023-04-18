package fr.funixgaming.api.core.crud.doc.dtos;

import fr.funixgaming.api.core.crud.doc.enums.TestEnum;
import fr.funixgaming.api.core.crud.dtos.ApiDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TestDTO extends ApiDTO {
    private String data;
    private Integer number;
    private Date date;
    private Float aFloat;
    private Double aDouble;
    private Boolean aBoolean;
    private TestEnum testEnum;
}
