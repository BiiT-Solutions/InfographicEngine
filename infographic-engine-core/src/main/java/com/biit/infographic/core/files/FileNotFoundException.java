package com.biit.infographic.core.files;

import com.biit.logger.ExceptionType;
import com.biit.server.exceptions.NotFoundException;

import java.io.Serial;

public class FileNotFoundException extends NotFoundException {
    @Serial
    private static final long serialVersionUID = 7131894111678894371L;

    public FileNotFoundException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public FileNotFoundException(Class<?> clazz, String message) {
        super(clazz, message);
    }

    public FileNotFoundException(Class<?> clazz) {
        this(clazz, "GlobalVariables not found");
    }

    public FileNotFoundException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
