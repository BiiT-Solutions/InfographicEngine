package com.biit.infographic.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;

public class MalformedConditionException extends LoggedException {
    private static final long serialVersionUID = -2895387942943473431L;

    public MalformedConditionException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public MalformedConditionException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.SEVERE, HttpStatus.BAD_REQUEST);
    }

    public MalformedConditionException(Class<?> clazz) {
        this(clazz, "Parameter is invalid");
    }

    public MalformedConditionException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
