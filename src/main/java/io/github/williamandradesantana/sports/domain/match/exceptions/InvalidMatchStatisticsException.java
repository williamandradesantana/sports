package io.github.williamandradesantana.sports.domain.match.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidMatchStatisticsException extends DomainException {
    public InvalidMatchStatisticsException(String message) {
        super(message);
    }
}
