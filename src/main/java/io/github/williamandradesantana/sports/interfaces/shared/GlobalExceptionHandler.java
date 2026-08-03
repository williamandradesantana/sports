package io.github.williamandradesantana.sports.interfaces.shared;

import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidPasswordException;
import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidPermissionDescriptionException;
import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidPermissionException;
import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse> handleInvalidUsernameException(InvalidUsernameException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            Instant.now(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse> handleInvalidPasswordException(InvalidPasswordException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            Instant.now(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPermissionDescriptionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse> handleInvalidPermissionDescriptionException(InvalidPermissionDescriptionException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            Instant.now(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPermissionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionResponse> handleInvalidPermissionException(InvalidPermissionException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            Instant.now(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            Instant.now(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
            Instant.now(),
            ex.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
