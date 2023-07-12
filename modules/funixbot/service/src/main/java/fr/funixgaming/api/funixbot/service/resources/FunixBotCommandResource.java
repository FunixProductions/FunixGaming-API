package fr.funixgaming.api.funixbot.service.resources;

import com.funixproductions.core.crud.resources.ApiResource;
import fr.funixgaming.api.funixbot.client.clients.FunixBotCommandClient;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.funixbot.service.services.FunixBotCommandsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funixbot/command")
public class FunixBotCommandResource extends ApiResource<FunixBotCommandDTO, FunixBotCommandsService> implements FunixBotCommandClient {

    public FunixBotCommandResource(FunixBotCommandsService service) {
        super(service);
    }

}
