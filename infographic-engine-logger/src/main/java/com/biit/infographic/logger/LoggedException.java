package com.biit.infographic.logger;


import org.springframework.http.HttpStatus;

public class LoggedException extends RuntimeException {
    private HttpStatus status;

    protected LoggedException(Class<?> clazz, String message, ExceptionType type, HttpStatus status) {
        super(message);
        this.status = status;
        final String className = clazz.getName();
        switch (type) {
            case INFO:
               InfograpicEngineLogger.info(className, message);
                break;
            case WARNING:
                InfograpicEngineLogger.warning(className, message);
                break;
            case SEVERE:
                InfograpicEngineLogger.severe(className, message);
                break;
            default:
                InfograpicEngineLogger.debug(className, message);
                break;
        }
    }

    protected LoggedException(Class<?> clazz, Throwable e, HttpStatus status) {
        this(clazz, e);
        this.status = status;
    }

    protected LoggedException(Class<?> clazz, Throwable e) {
        super(e);
        InfograpicEngineLogger.errorMessage(clazz, e);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
