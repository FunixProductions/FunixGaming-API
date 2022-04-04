package fr.funixgaming.api.server.funixbot.services;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.server.funixbot.entities.FunixBotUserExperience;
import fr.funixgaming.api.server.funixbot.mappers.FunixBotUserExperienceMapper;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotUserExperienceRepository;
import org.springframework.stereotype.Service;

@Service
public class FunixBotUserExperienceService extends ApiService<FunixBotUserExperienceDTO, FunixBotUserExperience, FunixBotUserExperienceMapper, FunixBotUserExperienceRepository> {
    public FunixBotUserExperienceService(FunixBotUserExperienceRepository repository, FunixBotUserExperienceMapper mapper) {
        super(repository, mapper);
    }
}
