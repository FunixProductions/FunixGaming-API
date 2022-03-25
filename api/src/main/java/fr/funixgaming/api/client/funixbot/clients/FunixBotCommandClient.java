package fr.funixgaming.api.client.funixbot.clients;

import fr.funixgaming.api.core.clients.CrudClient;
import fr.funixgaming.api.client.funixbot.dtos.FunixBotCommandDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "/funixbot/commands", url = "${app.domain.url}")
public interface FunixBotCommandClient extends CrudClient<FunixBotCommandDTO> {

    @GetMapping
    FunixBotCommandDTO findByCommandName(@RequestParam String command);

}
