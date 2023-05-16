package fr.funixgaming.api.client.funixbot.clients;

import com.funixproductions.core.crud.clients.CrudClient;
import fr.funixgaming.api.client.config.FeignConfig;
import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixBotCommands",
        url = "${funixgaming.api.app-domain-url}",
        path = "/funixbot/command/",
        configuration = FeignConfig.class
)
public interface FunixBotCommandClient extends CrudClient<FunixBotCommandDTO> {
}
