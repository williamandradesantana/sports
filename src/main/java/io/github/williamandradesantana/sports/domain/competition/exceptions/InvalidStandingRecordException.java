package io.github.williamandradesantana.sports.domain.competition.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidStandingRecordException extends DomainException {
    public InvalidStandingRecordException(String message) {
        super(message);
    }
}
