package fr.funixgaming.api.server.funixbot.services;

import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserDTO;
import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserExperienceDTO;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.core.exceptions.ApiNotFoundException;
import fr.funixgaming.api.server.funixbot.entities.user.FunixBotUser;
import fr.funixgaming.api.server.funixbot.entities.user.FunixBotUserExperience;
import fr.funixgaming.api.server.funixbot.mappers.FunixBotUserMapper;
import fr.funixgaming.api.server.funixbot.mappers.user.FunixBotUserExperienceMapper;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotUserRepository;
import fr.funixgaming.api.server.funixbot.repositories.user.FunixBotUserExperienceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FunixBotUserService extends ApiService<FunixBotUserDTO, FunixBotUser, FunixBotUserMapper, FunixBotUserRepository> {

    private final FunixBotUserExperienceMapper experienceMapper;
    private final FunixBotUserExperienceRepository experienceRepository;

    public FunixBotUserService(FunixBotUserRepository repository,
                               FunixBotUserMapper mapper,
                               FunixBotUserExperienceMapper experienceMapper,
                               FunixBotUserExperienceRepository experienceRepository) {
        super(repository, mapper);
        this.experienceMapper = experienceMapper;
        this.experienceRepository = experienceRepository;
    }

    @Override
    public FunixBotUserDTO create(FunixBotUserDTO request) {
        FunixBotUserExperience userExperience = experienceMapper.toEntity(request.getUserExperience());
        userExperience = experienceRepository.save(userExperience);
        request.setUserExperience(experienceMapper.toDto(userExperience));

        return super.create(request);
    }

    @Override
    public FunixBotUserDTO update(FunixBotUserDTO request) {
        final FunixBotUserExperienceDTO experienceDTO = ApiService.patch(request.getUserExperience(), experienceMapper, experienceRepository);

        if (request.getUserExperience().getId() == null) {
            throw new ApiNotFoundException("L'id %s funixbot experience n'existe pas.");
        }

        request.setUserExperience(experienceDTO);
        return super.update(request);
    }
}
