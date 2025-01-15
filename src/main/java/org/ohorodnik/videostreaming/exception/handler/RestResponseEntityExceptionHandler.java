package org.ohorodnik.videostreaming.exception.handler;

import org.ohorodnik.videostreaming.exception.MetadataNotFoundException;
import org.ohorodnik.videostreaming.exception.StorageException;
import org.ohorodnik.videostreaming.exception.VideoNotFoundException;
import org.ohorodnik.videostreaming.exception.dto.ErrorMessage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ErrorMessage> storageException(StorageException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());

        return ResponseEntity.unprocessableEntity().body(errorMessage);
    }

    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<ErrorMessage> videoNotFoundException(VideoNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(MetadataNotFoundException.class)
    public ResponseEntity<ErrorMessage> metadataNotFoundException(MetadataNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ErrorMessage> emptyResultDataAccessException(EmptyResultDataAccessException exception) {
        ErrorMessage errorMessage = new ErrorMessage("No result found for current query");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
