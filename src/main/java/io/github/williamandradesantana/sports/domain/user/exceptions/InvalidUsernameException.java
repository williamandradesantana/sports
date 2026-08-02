package io.github.williamandradesantana.sports.domain.user.exceptions;

public class InvalidUsernameException extends RuntimeException {
    public InvalidUsernameException() {
        super("Username cannot be null or blank");
    }
}
