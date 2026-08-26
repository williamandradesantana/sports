package io.github.williamandradesantana.sports.interfaces.integrity.dto;

import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessment;

import java.time.OffsetDateTime;
import java.util.List;

public record IntegrityAssessmentResponse(
        int score, String riskLevel, List<IntegrityFactorResponse> factors, OffsetDateTime assessedAt
) {
    public static IntegrityAssessmentResponse from(IntegrityAssessment assessment) {
        return new IntegrityAssessmentResponse(
            assessment.getScore(), assessment.getRiskLevel().name(),
            assessment.getFactors().stream().map(IntegrityFactorResponse::from).toList(),
            assessment.getAssessedAt()
        );
    }
}
