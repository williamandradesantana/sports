package io.github.williamandradesantana.sports.domain.user.exceptions;

public class InvalidPermissionDescriptionException extends RuntimeException {
    public InvalidPermissionDescriptionException() {
        super("Permission description is invalid");
    }
}
