package io.github.williamandradesantana.sports.domain.user.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidPermissionDescriptionException extends DomainException {
    public InvalidPermissionDescriptionException() {
        super("Permission description is invalid");
    }
}
