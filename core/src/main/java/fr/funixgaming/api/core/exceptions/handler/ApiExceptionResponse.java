package fr.funixgaming.api.core.exceptions.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiExceptionResponse {
    private final String message;
    private final Integer code;
}
