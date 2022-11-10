package fr.funixgaming.api.client.funixbot.clients;

import fr.funixgaming.api.client.funixbot.dtos.FunixBotUserExperienceDTO;
import fr.funixgaming.api.core.crud.clients.CrudClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "FunixBotUserExperience",
        url = "${funix.api.app-domain-url}",
        path = "/funixbot/user/exp/"
)
public interface FunixBotUserExperienceClient extends CrudClient<FunixBotUserExperienceDTO> {

    @GetMapping("rank")
    Integer getRank(@RequestParam String twitchUserId);

}
