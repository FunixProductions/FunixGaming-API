package fr.funixgaming.api.server.funixbot.repositories;

import fr.funixgaming.api.core.repositories.ApiRepository;
import fr.funixgaming.api.server.funixbot.entities.FunixBotCommand;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FunixBotCommandRepository extends ApiRepository<FunixBotCommand> {
    Optional<FunixBotCommand> findByCommand(String command);
}
