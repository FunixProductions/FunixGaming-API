package fr.funixgaming.api.funixbot.service.repositories;

import com.funixproductions.core.crud.repositories.ApiRepository;
import fr.funixgaming.api.funixbot.service.entities.FunixBotCommand;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FunixBotCommandRepository extends ApiRepository<FunixBotCommand> {
    Optional<FunixBotCommand> findByCommand(String command);
}
