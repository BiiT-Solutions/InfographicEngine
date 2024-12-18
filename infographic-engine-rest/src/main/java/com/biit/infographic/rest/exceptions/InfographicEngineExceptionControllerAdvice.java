package com.biit.infographic.rest.exceptions;

import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.exceptions.FormNotFoundException;
import com.biit.infographic.core.exceptions.InfographicNotFoundException;
import com.biit.infographic.core.exceptions.MalformedTemplateException;
import com.biit.server.exceptions.ErrorResponse;
import com.biit.server.exceptions.NotFoundException;
import com.biit.server.exceptions.ServerExceptionControllerAdvice;
import com.biit.server.logger.RestServerExceptionLogger;
import com.biit.server.utils.exceptions.EmptyPdfBodyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InfographicEngineExceptionControllerAdvice extends ServerExceptionControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("NOT_FOUND", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MalformedTemplateException.class)
    public ResponseEntity<?> malformedTemplateException(Exception ex) {
        RestServerExceptionLogger.errorMessage(this.getClass().getName(), ex);
        return new ResponseEntity<>(new ErrorResponse("Template not found", "template_not_found"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FormNotFoundException.class)
    public ResponseEntity<?> formNotFoundException(Exception ex) {
        RestServerExceptionLogger.severe(this.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("No data found", "form_not_found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyPdfBodyException.class)
    public ResponseEntity<?> emptyPdfBodyException(Exception ex) {
        RestServerExceptionLogger.severe(this.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("Pdf cannot be formed", "error_generating_pdf"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElementDoesNotExistsException.class)
    public ResponseEntity<?> elementDoesNotExistsException(Exception ex) {
        RestServerExceptionLogger.severe(this.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "element_does_not_exists", ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InfographicNotFoundException.class)
    public ResponseEntity<?> infographicNotFoundException(Exception ex) {
        RestServerExceptionLogger.severe(this.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), "report_not_found", ex), HttpStatus.NOT_FOUND);
    }
}
