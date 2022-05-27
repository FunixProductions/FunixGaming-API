package fr.funixgaming.api.server.mail.mappers;

import fr.funixgaming.api.client.mail.dtos.FunixMailDTO;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import fr.funixgaming.api.server.mail.entities.FunixMail;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface FunixMailMapper extends ApiMapper<FunixMail, FunixMailDTO> {

    @Override
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    FunixMail toEntity(FunixMailDTO dto);

    @Override
    @Mapping(target = "id", source = "uuid")
    FunixMailDTO toDto(FunixMail entity);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(FunixMail request, FunixMail toPatch);
}
