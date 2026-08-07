package io.github.williamandradesantana.sports.domain.user.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
