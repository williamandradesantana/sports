package io.github.williamandradesantana.sports.domain.user.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidPermissionException extends DomainException {
    public InvalidPermissionException(String message) {
        super(message);
    }
}
