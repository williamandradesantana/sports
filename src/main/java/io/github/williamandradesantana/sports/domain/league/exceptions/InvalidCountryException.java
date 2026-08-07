package io.github.williamandradesantana.sports.domain.league.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidCountryException extends DomainException {
    public InvalidCountryException(String message) {
        super(message);
    }
}
