package io.github.williamandradesantana.sports.domain.shared.exceptions;

public class InvalidExternalIdException extends RuntimeException {
    public InvalidExternalIdException(String message) {
        super(message);
    }
}
