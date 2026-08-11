package io.github.williamandradesantana.sports.domain.team.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidTeamNameException extends DomainException {
    public InvalidTeamNameException(String message) {
        super(message);
    }
}
