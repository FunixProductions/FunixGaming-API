package fr.funixgaming.api.client.funixbot.clients;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import fr.funixgaming.api.core.crud.clients.CrudClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixBotCommands",
        url = "${funix.api.app-domain-url}",
        path = "/funixbot/command/"
)
public interface FunixBotCommandClient extends CrudClient<FunixBotCommandDTO> {
}
