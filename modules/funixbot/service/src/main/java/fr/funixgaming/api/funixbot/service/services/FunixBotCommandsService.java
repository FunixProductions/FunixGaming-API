package fr.funixgaming.api.funixbot.service.services;

import com.funixproductions.core.crud.services.ApiService;
import com.funixproductions.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.funixbot.service.entities.FunixBotCommand;
import fr.funixgaming.api.funixbot.service.mappers.FunixBotCommandMapper;
import fr.funixgaming.api.funixbot.service.repositories.FunixBotCommandRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FunixBotCommandsService extends ApiService<FunixBotCommandDTO, FunixBotCommand, FunixBotCommandMapper, FunixBotCommandRepository> {

    public FunixBotCommandsService(FunixBotCommandRepository repository,
                                   FunixBotCommandMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public void beforeMappingToEntity(@NonNull Iterable<FunixBotCommandDTO> request) {
        for (FunixBotCommandDTO command : request) {
            if (command.getId() == null && super.getRepository().existsFunixBotCommandByCommandContainsIgnoreCase(command.getCommand())) {
                throw new ApiBadRequestException(String.format("La commande '%s' existe déjà.", command.getCommand()));
            }
            command.setCommand(command.getCommand().toLowerCase());
        }
    }

}
