package io.github.williamandradesantana.sports.domain.match.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidOddsException extends DomainException {
    public InvalidOddsException(String message) {
        super(message);
    }
}
