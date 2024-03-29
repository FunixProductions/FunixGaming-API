package fr.funixgaming.api.funixbot.client.clients;

import com.funixproductions.core.crud.clients.CrudClient;
import fr.funixgaming.api.core.client.clients.FeignTokenInterceptor;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotAutoMessageDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixBotAutoMessagesClient",
        url = "${funixgaming.api.funixbot.app-domain-url}",
        path = "/funixbot/automessages/",
        configuration = FeignTokenInterceptor.class
)
public interface FunixBotAutoMessagesClient extends CrudClient<FunixBotAutoMessageDTO> {
}
