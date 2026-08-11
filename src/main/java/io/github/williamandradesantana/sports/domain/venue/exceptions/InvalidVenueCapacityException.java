package io.github.williamandradesantana.sports.domain.venue.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidVenueCapacityException extends DomainException {
    public InvalidVenueCapacityException(String message) {
        super(message);
    }
}
