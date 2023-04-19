package fr.funixgaming.api.core.crud.doc.mappers;

import fr.funixgaming.api.core.crud.doc.dtos.TestSubDTO;
import fr.funixgaming.api.core.crud.doc.entities.TestSubEntity;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TestMapper.class)
public interface TestSubMapper extends ApiMapper<TestSubEntity, TestSubDTO> {
}
