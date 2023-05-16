package fr.funixgaming.api.client.funixbot.clients;

import com.funixproductions.core.crud.clients.CrudClient;
import fr.funixgaming.api.client.config.FeignConfig;
import fr.funixgaming.api.client.funixbot.dtos.FunixBotUserExperienceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "FunixBotUserExperience",
        url = "${funixgaming.api.app-domain-url}",
        path = "/funixbot/user/exp/",
        configuration = FeignConfig.class
)
public interface FunixBotUserExperienceClient extends CrudClient<FunixBotUserExperienceDTO> {

    @GetMapping("rank")
    Integer getRank(@RequestParam String twitchUserId);

}
