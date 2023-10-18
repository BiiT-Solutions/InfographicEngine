package com.biit.infographic.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;

public class ElementDoesNotExistsException extends LoggedException {
    private static final long serialVersionUID = -2898462947200473431L;

    public ElementDoesNotExistsException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public ElementDoesNotExistsException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.DEBUG, HttpStatus.BAD_REQUEST);
    }

    public ElementDoesNotExistsException(Class<?> clazz) {
        this(clazz, "Element does not exists!");
    }

    public ElementDoesNotExistsException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
