package com.biit.infographic.core.models.svg.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidCodeException extends LoggedException {
    private static final long serialVersionUID = 7138568131596899370L;

    public InvalidCodeException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public InvalidCodeException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.INFO);
    }

    public InvalidCodeException(Class<?> clazz) {
        this(clazz, "Role not found");
    }

    public InvalidCodeException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
