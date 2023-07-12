package fr.funixgaming.api.funixbot.service.services;

import com.funixproductions.core.crud.services.ApiService;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.funixbot.service.entities.FunixBotCommand;
import fr.funixgaming.api.funixbot.service.mappers.FunixBotCommandMapper;
import fr.funixgaming.api.funixbot.service.repositories.FunixBotCommandRepository;
import org.springframework.stereotype.Service;

@Service
public class FunixBotCommandsService extends ApiService<FunixBotCommandDTO, FunixBotCommand, FunixBotCommandMapper, FunixBotCommandRepository> {

    public FunixBotCommandsService(FunixBotCommandRepository repository,
                                   FunixBotCommandMapper mapper) {
        super(repository, mapper);
    }

}
