package fr.funixgaming.api.client.funixbot.clients;

import fr.funixgaming.api.client.config.FunixApiAuthConfig;
import fr.funixgaming.api.core.crud.clients.CrudClient;
import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixBotCommands",
        url = "${funix.api.app-domain-url}",
        path = "/funixbot/command/",
        configuration = FunixApiAuthConfig.class
)
public interface FunixBotCommandClient extends CrudClient<FunixBotCommandDTO> {
}
