package fr.funixgaming.api.server.funixbot.services;

import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserDTO;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.server.funixbot.entities.user.FunixBotUser;
import fr.funixgaming.api.server.funixbot.mappers.user.FunixBotUserMapper;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotUserRepository;
import org.springframework.stereotype.Service;

@Service
public class FunixBotUserService extends ApiService<FunixBotUserDTO, FunixBotUser, FunixBotUserMapper, FunixBotUserRepository> {
    public FunixBotUserService(FunixBotUserRepository repository, FunixBotUserMapper mapper) {
        super(repository, mapper);
    }
}
