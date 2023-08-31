package com.biit.infographic.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;

public class InvalidParameterException extends LoggedException {
    private static final long serialVersionUID = -2891387947200473431L;

    public InvalidParameterException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public InvalidParameterException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.SEVERE, HttpStatus.BAD_REQUEST);
    }

    public InvalidParameterException(Class<?> clazz) {
        this(clazz, "Parameter is invalid");
    }

    public InvalidParameterException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
