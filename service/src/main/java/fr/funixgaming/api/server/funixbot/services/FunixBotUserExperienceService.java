package fr.funixgaming.api.server.funixbot.services;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.core.crud.services.ApiService;
import fr.funixgaming.api.core.exceptions.ApiBadRequestException;
import fr.funixgaming.api.server.funixbot.entities.FunixBotUserExperience;
import fr.funixgaming.api.server.funixbot.mappers.FunixBotUserExperienceMapper;
import fr.funixgaming.api.server.funixbot.repositories.FunixBotUserExperienceRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FunixBotUserExperienceService extends ApiService<FunixBotUserExperienceDTO, FunixBotUserExperience, FunixBotUserExperienceMapper, FunixBotUserExperienceRepository> {
    public FunixBotUserExperienceService(FunixBotUserExperienceRepository repository, FunixBotUserExperienceMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public List<FunixBotUserExperienceDTO> getAll(String page, String elemsPerPage) {
        final int nbrPage;
        final int maxPerPage;

        try {
            if (Strings.isEmpty(page)) {
                nbrPage = 0;
            } else {
                nbrPage = Integer.parseInt(page);
            }

            if (Strings.isEmpty(elemsPerPage)) {
                maxPerPage = 300;
            } else {
                int max = Integer.parseInt(elemsPerPage);
                if (max > 300) {
                    max = 300;
                }
                maxPerPage = max;
            }
        } catch (NumberFormatException e) {
            throw new ApiBadRequestException("Vous avez rentré un nombre invalide.", e);
        }

        final Pageable pageable = PageRequest.of(nbrPage, maxPerPage, Sort.by(List.of(
                new Sort.Order(Sort.Direction.DESC, "level"),
                new Sort.Order(Sort.Direction.DESC, "xp")
        )));
        final List<FunixBotUserExperienceDTO> toSend = new ArrayList<>();

        for (final FunixBotUserExperience entity : getRepository().findAll(pageable)) {
            toSend.add(getMapper().toDto(entity));
        }
        return toSend;
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
