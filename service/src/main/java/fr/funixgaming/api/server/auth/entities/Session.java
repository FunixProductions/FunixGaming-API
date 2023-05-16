package fr.funixgaming.api.server.auth.entities;

import com.funixproductions.api.client.user.dtos.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Session {
    private final UserDTO user;
    private final String clientIp;
    private final HttpServletRequest request;
}
