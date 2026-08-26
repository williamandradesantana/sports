package io.github.williamandradesantana.sports.interfaces.integrity.dto;

import io.github.williamandradesantana.sports.domain.integrity.IntegrityFactor;

public record IntegrityFactorResponse(String code, String description, int points) {
    public static IntegrityFactorResponse from(IntegrityFactor factor) {
        return new IntegrityFactorResponse(factor.code(), factor.description(), factor.points());
    }
}
