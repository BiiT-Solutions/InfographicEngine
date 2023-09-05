package com.biit.infographic.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;

public class FormNotFoundException extends LoggedException {
    private static final long serialVersionUID = -2898472947200473410L;

    public FormNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public FormNotFoundException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.SEVERE, HttpStatus.BAD_REQUEST);
    }

    public FormNotFoundException(Class<?> clazz) {
        this(clazz, "Element does not exists!");
    }

    public FormNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
