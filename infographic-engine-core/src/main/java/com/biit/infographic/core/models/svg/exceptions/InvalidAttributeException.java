package com.biit.infographic.core.models.svg.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidAttributeException extends LoggedException {
    private static final long serialVersionUID = 7132995131672899370L;

    public InvalidAttributeException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public InvalidAttributeException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.INFO);
    }

    public InvalidAttributeException(Class<?> clazz) {
        this(clazz, "Role not found");
    }

    public InvalidAttributeException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
