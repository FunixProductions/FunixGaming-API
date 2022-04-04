package fr.funixgaming.api.server.funixbot.resources;

import fr.funixgaming.api.client.funixbot.clients.FunixBotUserExperienceClient;
import fr.funixgaming.api.client.funixbot.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import fr.funixgaming.api.server.funixbot.services.FunixBotUserExperienceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funixbot/users/xp")
public class FunixBotUseExperienceResource extends ApiResource<FunixBotUserExperienceDTO, FunixBotUserExperienceService> implements FunixBotUserExperienceClient {
    public FunixBotUseExperienceResource(FunixBotUserExperienceService funixBotUserExperienceService) {
        super(funixBotUserExperienceService);
    }
}
