package fr.funixgaming.api.client.funixbot.clients;

import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserDTO;
import fr.funixgaming.api.core.crud.clients.CrudClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "FunixBotUser", url = "${app.domain.url}", path = "/funixbot/user/")
public interface FunixBotUserClient extends CrudClient<FunixBotUserDTO> {
}
