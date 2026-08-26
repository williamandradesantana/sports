package io.github.williamandradesantana.sports.application.integrity;

public class MatchNotAssessableException extends RuntimeException {
    public MatchNotAssessableException(String message) {
        super(message);
    }
}
