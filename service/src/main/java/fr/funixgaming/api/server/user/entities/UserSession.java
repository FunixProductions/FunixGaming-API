package fr.funixgaming.api.server.user.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSession {

    private final User user;
    private final String ip;

}
