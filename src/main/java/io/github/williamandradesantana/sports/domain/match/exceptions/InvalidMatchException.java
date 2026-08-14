package io.github.williamandradesantana.sports.domain.match.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidMatchException extends DomainException {
    public InvalidMatchException(String message) {
        super(message);
    }
}
