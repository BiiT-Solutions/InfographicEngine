package com.biit.infographic.rest.exceptions;

import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.exceptions.FormNotFoundException;
import com.biit.infographic.core.exceptions.InfographicNotFoundException;
import com.biit.infographic.core.exceptions.InvalidParameterException;
import com.biit.infographic.core.exceptions.MalformedTemplateException;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.kafka.exceptions.InvalidEventException;
import com.biit.logger.mail.exceptions.EmailNotSentException;
import com.biit.server.exceptions.ErrorResponse;
import com.biit.server.exceptions.ServerExceptionControllerAdvice;
import com.biit.server.utils.exceptions.EmptyPdfBodyException;
import com.biit.usermanager.client.exceptions.ElementNotFoundException;
import com.biit.usermanager.client.exceptions.InvalidConfigurationException;
import com.biit.usermanager.client.exceptions.InvalidValueException;
import com.biit.usermanager.logger.UserManagerLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InfographicEngineExceptionControllerAdvice extends ServerExceptionControllerAdvice {

    @ExceptionHandler(MalformedTemplateException.class)
    public ResponseEntity<Object> malformedTemplateException(Exception ex) {
        InfographicEngineLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("Template not found", "template_not_found"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FormNotFoundException.class)
    public ResponseEntity<Object> formNotFoundException(Exception ex) {
        InfographicEngineLogger.severe(this.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("No data found", "form_not_found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyPdfBodyException.class)
    public ResponseEntity<Object> emptyPdfBodyException(Exception ex) {
        InfographicEngineLogger.severe(this.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("Pdf cannot be formed", "error_generating_pdf"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElementDoesNotExistsException.class)
    public ResponseEntity<Object> elementDoesNotExistsException(Exception ex) {
        InfographicEngineLogger.severe(this.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "not_found", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InfographicNotFoundException.class)
    public ResponseEntity<Object> infographicNotFoundException(Exception ex) {
        InfographicEngineLogger.severe(this.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "report_not_found", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Object> invalidParameterException(Exception ex) {
        InfographicEngineLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("Invalid parameter!", "invalid_parameter", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidEventException.class)
    public ResponseEntity<Object> invalidEventException(Exception ex) {
        InfographicEngineLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "cannot_connect_to_kafka", ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<Object> elementNotFoundException(Exception ex) {
        InfographicEngineLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "not_found", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidConfigurationException.class)
    public ResponseEntity<Object> invalidConfigurationException(Exception ex) {
        InfographicEngineLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_configuration_exception", ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidValueException.class)
    public ResponseEntity<Object> invalidValueException(Exception ex) {
        InfographicEngineLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "invalid_parameter", ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotSentException.class)
    public ResponseEntity<Object> emailNotSentException(Exception ex) {
        UserManagerLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "email_not_sent"), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
