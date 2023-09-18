package com.biit.infographic.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;

public class MalformedTemplateException extends LoggedException {
    private static final long serialVersionUID = -2895387942201473431L;

    public MalformedTemplateException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public MalformedTemplateException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.SEVERE, HttpStatus.BAD_REQUEST);
    }

    public MalformedTemplateException(Class<?> clazz) {
        this(clazz, "Parameter is invalid");
    }

    public MalformedTemplateException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
