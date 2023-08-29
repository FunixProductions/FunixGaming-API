package fr.funixgaming.api.funixbot.service.mappers;

import com.funixproductions.core.crud.mappers.ApiMapper;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotAutoMessageDTO;
import fr.funixgaming.api.funixbot.service.entities.FunixBotAutoMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FunixBotAutoMessagesMapper extends ApiMapper<FunixBotAutoMessage, FunixBotAutoMessageDTO> {
}
