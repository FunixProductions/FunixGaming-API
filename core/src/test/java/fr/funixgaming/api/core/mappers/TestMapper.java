package fr.funixgaming.api.core.mappers;

import fr.funixgaming.api.core.dtos.TestDTO;
import fr.funixgaming.api.core.entities.Test;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TestMapper extends ApiMapper<Test, TestDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    Test toEntity(TestDTO dto);

    @Override
    @InheritInverseConfiguration
    TestDTO toDto(Test entity);

    @Override
    void patch(Test request, Test toPatch);
}
