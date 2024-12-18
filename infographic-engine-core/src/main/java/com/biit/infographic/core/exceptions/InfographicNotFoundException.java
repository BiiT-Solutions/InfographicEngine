package com.biit.infographic.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class InfographicNotFoundException extends LoggedException {

    @Serial
    private static final long serialVersionUID = 5636450789353772632L;

    public InfographicNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public InfographicNotFoundException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.DEBUG, HttpStatus.NOT_FOUND);
    }

    public InfographicNotFoundException(Class<?> clazz) {
        this(clazz, "No infographics found!");
    }

    public InfographicNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
