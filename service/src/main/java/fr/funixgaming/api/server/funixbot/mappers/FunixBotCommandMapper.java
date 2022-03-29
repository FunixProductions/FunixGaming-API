package fr.funixgaming.api.server.funixbot.mappers;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.core.crud.mappers.ApiMapper;
import fr.funixgaming.api.server.funixbot.entities.FunixBotCommand;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FunixBotCommandMapper extends ApiMapper<FunixBotCommand, FunixBotCommandDTO> {
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    FunixBotCommand toEntity(FunixBotCommandDTO dto);

    @Mapping(target = "id", source = "uuid")
    FunixBotCommandDTO toDto(FunixBotCommand entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patch(FunixBotCommand request, @MappingTarget FunixBotCommand toPatch);
}
