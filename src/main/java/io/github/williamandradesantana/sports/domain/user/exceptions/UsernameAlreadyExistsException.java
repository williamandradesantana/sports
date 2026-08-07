package io.github.williamandradesantana.sports.domain.user.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class UsernameAlreadyExistsException extends DomainException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
