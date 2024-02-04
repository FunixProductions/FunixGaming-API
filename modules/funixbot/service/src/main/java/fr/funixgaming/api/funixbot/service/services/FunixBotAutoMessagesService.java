package fr.funixgaming.api.funixbot.service.services;

import com.funixproductions.core.crud.services.ApiService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotAutoMessageDTO;
import fr.funixgaming.api.funixbot.service.entities.FunixBotAutoMessage;
import fr.funixgaming.api.funixbot.service.mappers.FunixBotAutoMessagesMapper;
import fr.funixgaming.api.funixbot.service.repositories.FunixBotAutoMessagesRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class FunixBotAutoMessagesService extends ApiService<FunixBotAutoMessageDTO, FunixBotAutoMessage, FunixBotAutoMessagesMapper, FunixBotAutoMessagesRepository> {

    public FunixBotAutoMessagesService(FunixBotAutoMessagesRepository repository, FunixBotAutoMessagesMapper mapper) {
        super(repository, mapper);
    }

}
