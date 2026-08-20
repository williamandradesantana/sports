package io.github.williamandradesantana.sports.domain.competition.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidStandingException extends DomainException {
    public InvalidStandingException(String message) {
        super(message);
    }
}
