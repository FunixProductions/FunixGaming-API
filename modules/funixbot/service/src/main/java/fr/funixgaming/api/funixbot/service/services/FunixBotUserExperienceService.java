package fr.funixgaming.api.funixbot.service.services;

import com.funixproductions.core.crud.services.ApiService;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.funixbot.service.entities.FunixBotUserExperience;
import fr.funixgaming.api.funixbot.service.mappers.FunixBotUserExperienceMapper;
import fr.funixgaming.api.funixbot.service.repositories.FunixBotUserExperienceRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunixBotUserExperienceService extends ApiService<FunixBotUserExperienceDTO, FunixBotUserExperience, FunixBotUserExperienceMapper, FunixBotUserExperienceRepository> {
    public FunixBotUserExperienceService(FunixBotUserExperienceRepository repository, FunixBotUserExperienceMapper mapper) {
        super(repository, mapper);
    }

    public Integer getRank(final String twitchUserId) {
        int rank = 1;
        final List<FunixBotUserExperience> users = getRepository().findAll(
                Sort.by(List.of(
                        new Sort.Order(Sort.Direction.DESC, "level"),
                        new Sort.Order(Sort.Direction.DESC, "xp")
                ))
        );

        for (final FunixBotUserExperience experience : users) {
            if (experience.getTwitchUserId().equals(twitchUserId)) {
                return rank;
            } else {
                ++rank;
            }
        }

        return 0;
    }
}
