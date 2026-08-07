package io.github.williamandradesantana.sports.domain.user.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidUsernameException extends DomainException {
    public InvalidUsernameException() {
        super("Username cannot be null or blank");
    }
}
