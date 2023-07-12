package fr.funixgaming.api.funixbot.client.clients;

import com.funixproductions.core.crud.clients.CrudClient;
import fr.funixgaming.api.core.client.clients.FeignTokenInterceptor;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotCommandDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixBotCommands",
        url = "${funixgaming.api.funixbot.app-domain-url}",
        path = "/funixbot/command/",
        configuration = FeignTokenInterceptor.class
)
public interface FunixBotCommandClient extends CrudClient<FunixBotCommandDTO> {
}
