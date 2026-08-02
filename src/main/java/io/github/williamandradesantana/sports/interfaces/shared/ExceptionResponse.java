package io.github.williamandradesantana.sports.interfaces.shared;

import java.time.Instant;

public record ExceptionResponse(Instant timestamp, String message, String details) {
}
