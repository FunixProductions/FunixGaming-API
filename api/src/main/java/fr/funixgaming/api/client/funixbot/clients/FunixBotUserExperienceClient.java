package fr.funixgaming.api.client.funixbot.clients;

import fr.funixgaming.api.client.config.FunixApiAuthConfig;
import fr.funixgaming.api.client.funixbot.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.core.crud.clients.CrudClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "FunixBotUserExperience",
        url = "${funix.api.app-domain-url}",
        path = "/funixbot/user/exp/",
        configuration = FunixApiAuthConfig.class
)
public interface FunixBotUserExperienceClient extends CrudClient<FunixBotUserExperienceDTO> {
}
