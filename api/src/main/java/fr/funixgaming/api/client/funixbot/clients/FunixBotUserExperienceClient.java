package fr.funixgaming.api.client.funixbot.clients;

import fr.funixgaming.api.client.funixbot.dtos.user.FunixBotUserExperienceDTO;
import fr.funixgaming.api.core.crud.clients.CrudClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "FunixBotUserExperience", url = "${app.domain.url}", path = "/funixbot/user/exp/")
public interface FunixBotUserExperienceClient extends CrudClient<FunixBotUserExperienceDTO> {
}
