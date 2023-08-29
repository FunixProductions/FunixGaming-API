package fr.funixgaming.api.funixbot.service.resources;

import com.funixproductions.core.crud.resources.ApiResource;
import fr.funixgaming.api.funixbot.client.clients.FunixBotAutoMessagesClient;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotAutoMessageDTO;
import fr.funixgaming.api.funixbot.service.services.FunixBotAutoMessagesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/funixbot/automessages")
public class FunixBotAutoMessagesResource extends ApiResource<FunixBotAutoMessageDTO, FunixBotAutoMessagesService> implements FunixBotAutoMessagesClient {

    public FunixBotAutoMessagesResource(FunixBotAutoMessagesService service) {
        super(service);
    }

}
