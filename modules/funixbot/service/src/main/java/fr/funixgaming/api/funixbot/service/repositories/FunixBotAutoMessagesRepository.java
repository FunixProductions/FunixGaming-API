package fr.funixgaming.api.funixbot.service.repositories;

import com.funixproductions.core.crud.repositories.ApiRepository;
import fr.funixgaming.api.funixbot.service.entities.FunixBotAutoMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface FunixBotAutoMessagesRepository extends ApiRepository<FunixBotAutoMessage> {
}
