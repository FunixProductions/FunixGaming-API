package fr.funixgaming.api.server.funixbot.resources;

import com.funixproductions.core.crud.resources.ApiResource;
import fr.funixgaming.api.client.funixbot.clients.FunixBotUserExperienceClient;
import fr.funixgaming.api.client.funixbot.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.server.funixbot.services.FunixBotUserExperienceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funixbot/user/exp")
public class FunixBotUserExperienceResource extends ApiResource<FunixBotUserExperienceDTO, FunixBotUserExperienceService> implements FunixBotUserExperienceClient {
    public FunixBotUserExperienceResource(FunixBotUserExperienceService funixBotUserExperienceService) {
        super(funixBotUserExperienceService);
    }

    @Override
    public Integer getRank(String twitchUserId) {
        return this.getService().getRank(twitchUserId);
    }
}
