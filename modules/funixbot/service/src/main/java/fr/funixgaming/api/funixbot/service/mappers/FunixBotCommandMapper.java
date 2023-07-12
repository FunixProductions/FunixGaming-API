package fr.funixgaming.api.funixbot.service.mappers;

import com.funixproductions.core.crud.mappers.ApiMapper;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.funixbot.service.entities.FunixBotCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FunixBotCommandMapper extends ApiMapper<FunixBotCommand, FunixBotCommandDTO> {
}
