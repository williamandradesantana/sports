package io.github.williamandradesantana.sports.domain.user.exceptions;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Password must be at least 8 characters long");
    }
}
