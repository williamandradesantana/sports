package io.github.williamandradesantana.sports.domain.audit.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidAccessLogException extends DomainException {
    public InvalidAccessLogException(String message) {
        super(message);
    }
}
