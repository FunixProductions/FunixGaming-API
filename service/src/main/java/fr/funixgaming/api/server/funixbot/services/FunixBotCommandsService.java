package fr.funixgaming.api.server.funixbot.services;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.core.services.ApiService;
import fr.funixgaming.api.server.funixbot.entities.FunixBotCommand;
import fr.funixgaming.api.server.funixbot.mappers.FunixBotCommandMapper;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotCommandRepository;
import org.springframework.stereotype.Service;

@Service
public class FunixBotCommandsService extends ApiService<FunixBotCommandDTO, FunixBotCommand, FunixBotCommandMapper, FunixBotCommandRepository> {
    public FunixBotCommandsService(FunixBotCommandRepository repository,
                                   FunixBotCommandMapper mapper) {
        super(repository, mapper);
    }
}
