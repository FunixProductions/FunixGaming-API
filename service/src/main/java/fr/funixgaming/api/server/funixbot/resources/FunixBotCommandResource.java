package fr.funixgaming.api.server.funixbot.resources;

import fr.funixgaming.api.client.funixbot.clients.FunixBotCommandsClient;
import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.core.resource.ApiResource;
import fr.funixgaming.api.server.funixbot.services.FunixBotCommandsService;

public class FunixBotCommandResource extends ApiResource<FunixBotCommandDTO, FunixBotCommandsService> implements FunixBotCommandsClient  {
    public FunixBotCommandResource(FunixBotCommandsService service) {
        super(service);
    }
}
