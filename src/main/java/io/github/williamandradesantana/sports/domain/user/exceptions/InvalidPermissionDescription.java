package io.github.williamandradesantana.sports.domain.user.exceptions;

public class InvalidPermissionDescription extends RuntimeException {
    public InvalidPermissionDescription() {
        super("Permission description is invalid");
    }
}
