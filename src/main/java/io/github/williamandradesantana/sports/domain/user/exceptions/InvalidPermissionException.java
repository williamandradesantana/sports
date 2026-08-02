package io.github.williamandradesantana.sports.domain.user.exceptions;

public class InvalidPermissionException extends RuntimeException {
    public InvalidPermissionException(String message) {
        super(message);
    }
}
