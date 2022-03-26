package fr.funixgaming.api.client.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    USER("USER"),
    MODERATOR("MODERATOR"),
    ADMIN("ADMIN");

    private final String role;
}
