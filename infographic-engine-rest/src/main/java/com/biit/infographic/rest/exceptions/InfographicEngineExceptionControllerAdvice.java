package com.biit.infographic.rest.exceptions;

import com.biit.infographic.core.exceptions.FormNotFoundException;
import com.biit.infographic.core.exceptions.MalformedTemplateException;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.server.exceptions.NotFoundException;
import com.biit.server.exceptions.ServerExceptionControllerAdvice;
import com.biit.server.logger.RestServerExceptionLogger;
import com.biit.server.security.rest.exceptions.InvalidPasswordException;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InfographicEngineExceptionControllerAdvice extends ServerExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("NOT_FOUND", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Object> invalidPasswordException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("User not found for provided credentials"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAttributeException.class)
    public ResponseEntity<?> invalidAttributeException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        // Cannot have message as suggested here:
        // https://stackoverflow.com/questions/32123540/spring-exceptionhandler-and-httpmediatypenotacceptableexception
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MalformedTemplateException.class)
    public ResponseEntity<?> malformedTemplateException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorMessage("Template is malformed"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FormNotFoundException.class)
    public ResponseEntity<?> formNotFoundException(Exception ex) {
        RestServerExceptionLogger.severe(this.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(new ErrorMessage("No data found"), HttpStatus.NOT_FOUND);
    }
}
