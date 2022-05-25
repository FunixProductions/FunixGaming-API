package fr.funixgaming.api.server.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LineCommand {
    GET_API_USER("GetApiUser", "Envoie un mail avec les infos du compte api au propriétaire de l'api.", "GetApiUser");

    private final String command;
    private final String description;
    private final String example;
}
