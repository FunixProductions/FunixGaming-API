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

    private static final int MAX_LEN_MESSAGE = 500;
    private static final int MAX_LEN_COMMAND = 30;
    private static final String ERROR_MESSAGE_COMMAND_MAX_LEN = String.format("La commande ne peut pas dépasser %d caractères.", MAX_LEN_COMMAND);
    private static final String ERROR_MESSAGE_MAX_LEN = String.format("Le message de la commande ne peut pas dépasser %d caractères.", MAX_LEN_MESSAGE);
    private static final String ERROR_MESSAGE_NOT_ALPHANUMERIC = "La commande ne peut contenir que des caractères alphanumériques.";
    private final Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");

    public FunixBotCommandsService(FunixBotCommandRepository repository,
                                   FunixBotCommandMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public void beforeMappingToEntity(@NonNull Iterable<FunixBotCommandDTO> request) {
        for (FunixBotCommandDTO command : request) {
            if (command.getMessage().length() > MAX_LEN_MESSAGE) {
                throw new ApiBadRequestException(ERROR_MESSAGE_MAX_LEN);
            }

            if (command.getCommand().startsWith("!")) {
                command.setCommand(command.getCommand().substring(1));
            }
            if (command.getCommand().length() > MAX_LEN_COMMAND) {
                throw new ApiBadRequestException(ERROR_MESSAGE_COMMAND_MAX_LEN);
            }

            if (!isAlphanumeric(command.getCommand())) {
                throw new ApiBadRequestException(ERROR_MESSAGE_NOT_ALPHANUMERIC);
            }

            if (super.getRepository().existsFunixBotCommandByCommandContainsIgnoreCase(command.getCommand())) {
                throw new ApiBadRequestException(String.format("La commande '%s' existe déjà.", command.getCommand()));
            }

            command.setCommand(command.getCommand().toLowerCase());
        }
    }

    private boolean isAlphanumeric(String input) {
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

}
