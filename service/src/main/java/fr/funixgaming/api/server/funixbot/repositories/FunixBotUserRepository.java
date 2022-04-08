package fr.funixgaming.api.server.funixbot.repositories;

import fr.funixgaming.api.core.crud.repositories.ApiRepository;
import fr.funixgaming.api.server.funixbot.entities.user.FunixBotUser;
import org.springframework.stereotype.Repository;

@Repository
public interface FunixBotUserRepository extends ApiRepository<FunixBotUser> {
}
