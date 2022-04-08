package fr.funixgaming.api.server.funixbot.resources;

import fr.funixgaming.api.client.funixbot.clients.FunixBotUserClient;
import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserDTO;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import fr.funixgaming.api.server.funixbot.services.FunixBotUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funixbot/user/")
public class FunixBotUserResource extends ApiResource<FunixBotUserDTO, FunixBotUserService> implements FunixBotUserClient {
    public FunixBotUserResource(FunixBotUserService funixBotUserService) {
        super(funixBotUserService);
    }
}
