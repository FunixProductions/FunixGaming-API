package fr.funixgaming.api.core.doc;

import fr.funixgaming.api.core.mappers.ApiMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TestMapper extends ApiMapper<Test, TestDTO> {
    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    Test toEntity(TestDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    TestDTO toDto(Test entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(Test request, @MappingTarget Test toPatch);
}
