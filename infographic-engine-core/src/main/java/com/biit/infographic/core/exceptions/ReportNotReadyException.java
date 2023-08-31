package com.biit.infographic.core.exceptions;

import com.biit.logger.ExceptionType;
import com.biit.server.logger.LoggedException;
import org.springframework.http.HttpStatus;

public class ReportNotReadyException extends LoggedException {
    private static final long serialVersionUID = -2890387947220470431L;

    public ReportNotReadyException(Class<?> clazz, String message, ExceptionType type) {
        super(clazz, message, type);
    }

    public ReportNotReadyException(Class<?> clazz, String message) {
        super(clazz, message, ExceptionType.SEVERE, HttpStatus.BAD_REQUEST);
    }

    public ReportNotReadyException(Class<?> clazz) {
        this(clazz, "Report is not ready");
    }

    public ReportNotReadyException(Class<?> clazz, Throwable e) {
        super(clazz, e);
    }
}
