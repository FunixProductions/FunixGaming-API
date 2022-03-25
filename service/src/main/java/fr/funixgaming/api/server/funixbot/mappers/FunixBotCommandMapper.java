package fr.funixgaming.api.server.funixbot.mappers;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.core.mappers.ApiMapper;
import fr.funixgaming.api.server.funixbot.entities.FunixBotCommand;
import org.mapstruct.*;

@Mapper
public interface FunixBotCommandMapper extends ApiMapper<FunixBotCommand, FunixBotCommandDTO> {
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    @Override
    FunixBotCommand toEntity(FunixBotCommandDTO dto);

    @InheritInverseConfiguration
    @Override
    FunixBotCommandDTO toDto(FunixBotCommand entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Override
    void patch(FunixBotCommand request, @MappingTarget FunixBotCommand toPatch);
}
