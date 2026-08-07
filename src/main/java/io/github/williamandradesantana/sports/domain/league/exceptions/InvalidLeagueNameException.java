package io.github.williamandradesantana.sports.domain.league.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidLeagueNameException extends DomainException {
    public InvalidLeagueNameException(String message) {
        super(message);
    }
}
