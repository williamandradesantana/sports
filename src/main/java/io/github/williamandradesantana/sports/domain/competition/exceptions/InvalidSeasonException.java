package io.github.williamandradesantana.sports.domain.competition.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidSeasonException extends DomainException {
    public InvalidSeasonException(String message) {
        super(message);
    }
}
