package fr.funixgaming.api.client.funixbot.clients;

import fr.funixgaming.api.core.clients.CrudClient;
import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "FunixBotCommands", url = "${app.domain.url}/funixbot/commands")
public interface FunixBotCommandClient extends CrudClient<FunixBotCommandDTO> {
}
