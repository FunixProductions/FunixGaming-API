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

    private static final int MAX_LEN_MESSAGE = 500;
    private static final int MAX_LEN_GAME_NAME = 100;
    private static final String ERROR_MESSAGE_GAME_NAME_MAX_LEN = String.format("Le nom du jeu ne peut pas dépasser %d caractères.", MAX_LEN_GAME_NAME);
    private static final String ERROR_MESSAGE_MAX_LEN = String.format("Le message ne peut pas dépasser %d caractères.", MAX_LEN_MESSAGE);

    public FunixBotAutoMessagesService(FunixBotAutoMessagesRepository repository, FunixBotAutoMessagesMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public void beforeMappingToEntity(@NonNull Iterable<FunixBotAutoMessageDTO> request) {
        for (FunixBotAutoMessageDTO message : request) {
            if (message.getMessage().length() > MAX_LEN_MESSAGE) {
                throw new ApiBadRequestException(ERROR_MESSAGE_MAX_LEN);
            }
            if (message.getGameName() != null && message.getGameName().length() > MAX_LEN_GAME_NAME) {
                throw new ApiBadRequestException(ERROR_MESSAGE_GAME_NAME_MAX_LEN);
            }
        }
    }
}
