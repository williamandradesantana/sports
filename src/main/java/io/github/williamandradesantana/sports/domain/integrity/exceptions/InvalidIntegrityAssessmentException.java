package io.github.williamandradesantana.sports.domain.integrity.exceptions;

import io.github.williamandradesantana.sports.domain.shared.exceptions.DomainException;

public class InvalidIntegrityAssessmentException extends DomainException {
    public InvalidIntegrityAssessmentException(String message) {
        super(message);
    }
}
