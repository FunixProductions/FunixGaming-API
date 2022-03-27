package fr.funixgaming.api.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApiBadRequestException extends ApiException {
    public ApiBadRequestException(String message, Throwable e) {
        super(message, e);
    }

    public ApiBadRequestException(String message) {
        super(message);
    }
}
