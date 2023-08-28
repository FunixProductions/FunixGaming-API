package fr.funixgaming.api.funixbot.service.repositories;

import com.funixproductions.core.crud.repositories.ApiRepository;
import fr.funixgaming.api.funixbot.service.entities.FunixBotCommand;
import org.springframework.stereotype.Repository;

@Repository
public interface FunixBotCommandRepository extends ApiRepository<FunixBotCommand> {
    boolean existsByCommand(String command);
}
