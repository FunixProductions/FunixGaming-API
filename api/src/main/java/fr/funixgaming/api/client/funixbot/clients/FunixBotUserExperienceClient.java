package fr.funixgaming.api.client.funixbot.clients;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.core.crud.clients.CrudClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "FunixBotUsersXP", url = "${app.domain.url}", path = "/funixbot/user/xp")
public interface FunixBotUserExperienceClient extends CrudClient<FunixBotUserExperienceDTO> {
}
