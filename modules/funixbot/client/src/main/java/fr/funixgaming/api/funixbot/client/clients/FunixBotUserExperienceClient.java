package fr.funixgaming.api.funixbot.client.clients;

import com.funixproductions.core.crud.clients.CrudClient;
import fr.funixgaming.api.core.client.clients.FeignTokenInterceptor;
import fr.funixgaming.api.funixbot.client.dtos.FunixBotUserExperienceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "FunixBotUserExperience",
        url = "${funixgaming.api.funixbot.app-domain-url}",
        path = "/funixbot/user/exp/",
        configuration = FeignTokenInterceptor.class
)
public interface FunixBotUserExperienceClient extends CrudClient<FunixBotUserExperienceDTO> {

    @GetMapping("rank")
    Integer getRank(@RequestParam String twitchUserId);

}
