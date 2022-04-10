package fr.funixgaming.api.server.funixbot.services.user;

import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserExperienceDTO;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.server.funixbot.entities.user.FunixBotUserExperience;
import fr.funixgaming.api.server.funixbot.mappers.user.FunixBotUserExperienceMapper;
import fr.funixgaming.api.server.funixbot.repositories.user.FunixBotUserExperienceRepository;
import org.springframework.stereotype.Service;

@Service
public class FunixBotUserExperienceService extends ApiService<FunixBotUserExperienceDTO, FunixBotUserExperience, FunixBotUserExperienceMapper, FunixBotUserExperienceRepository> {
    public FunixBotUserExperienceService(FunixBotUserExperienceRepository repository, FunixBotUserExperienceMapper mapper) {
        super(repository, mapper);
    }
}
