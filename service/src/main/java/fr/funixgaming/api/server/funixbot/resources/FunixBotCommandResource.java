package fr.funixgaming.api.server.funixbot.resources;

import fr.funixgaming.api.client.funixbot.clients.FunixBotCommandClient;
import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.core.crud.resources.ApiResource;
import fr.funixgaming.api.server.funixbot.services.FunixBotCommandsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funixbot/command")
public class FunixBotCommandResource extends ApiResource<FunixBotCommandDTO, FunixBotCommandsService> implements FunixBotCommandClient {

    public FunixBotCommandResource(FunixBotCommandsService service) {
        super(service);
    }

}
