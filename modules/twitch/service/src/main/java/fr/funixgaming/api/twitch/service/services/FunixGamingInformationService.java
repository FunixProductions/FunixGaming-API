package fr.funixgaming.api.twitch.service.services;

import com.funixproductions.api.user.client.clients.InternalUserCrudClient;
import com.funixproductions.api.user.client.dtos.UserDTO;
import com.funixproductions.core.crud.dtos.PageDTO;
import com.funixproductions.core.crud.enums.SearchOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class FunixGamingInformationService {

    private static final String FUNIX_GAMING_USERNAME = "funix";
    private final InternalUserCrudClient internalUserCrudClient;
    private UserDTO funixGamingUser = null;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void fetchUserInfos() {
        final PageDTO<UserDTO> search = this.internalUserCrudClient.getAll(
                "0",
                "1",
                String.format("username:%s:%s", SearchOperation.EQUALS, FUNIX_GAMING_USERNAME),
                ""
        );

        if (search.getContent().size() != 1 || !search.getContent().get(0).getUsername().equals(FUNIX_GAMING_USERNAME)) {
            log.error("Impossible de récupérer les informations de l'utilisateur funixgaming. 404 error");
        } else {
            this.funixGamingUser = search.getContent().get(0);
        }
    }

}
