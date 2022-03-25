package fr.funixgaming.api.server.funixbot.mappers;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.core.mappers.ApiMapper;
import fr.funixgaming.api.server.funixbot.entities.FunixBotCommand;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FunixBotCommandMapper extends ApiMapper<FunixBotCommand, FunixBotCommandDTO> {
    @Mapping(target = "uuid", source = "id")
    @Mapping(target = "id", ignore = true)
    FunixBotCommand toEntity(FunixBotCommandDTO dto);

    @InheritInverseConfiguration
    FunixBotCommandDTO toDto(FunixBotCommand entity);
}
