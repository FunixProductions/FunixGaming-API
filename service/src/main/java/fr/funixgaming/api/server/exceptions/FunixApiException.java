package fr.funixgaming.api.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FunixApiException extends RuntimeException {
    public FunixApiException(String message, Throwable e) {
        super(message, e);
    }
}
