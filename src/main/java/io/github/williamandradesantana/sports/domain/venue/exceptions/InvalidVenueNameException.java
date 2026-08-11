package io.github.williamandradesantana.sports.domain.venue.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidVenueNameException extends DomainException {
    public InvalidVenueNameException(String message) {
        super(message);
    }
}
