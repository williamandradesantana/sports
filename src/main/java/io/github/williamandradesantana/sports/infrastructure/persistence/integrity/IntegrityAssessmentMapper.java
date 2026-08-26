package io.github.williamandradesantana.sports.infrastructure.persistence.integrity;

import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessment;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityFactor;

import java.util.List;
import java.util.UUID;

public class IntegrityAssessmentMapper {

    public IntegrityAssessment toDomain(IntegrityAssessmentJpaEntity entity, List<IntegrityFactorJpaEntity> factorEntities) {
        List<IntegrityFactor> factors = factorEntities.stream()
                .map(f -> new IntegrityFactor(f.getCode(), f.getDescription(), f.getPoints()))
                .toList();

        return new IntegrityAssessment(entity.getId(), entity.getMatchId(), entity.getScore(), factors,
                entity.getAssessedAt());
    }

    public IntegrityAssessmentJpaEntity toJpaEntity(IntegrityAssessment assessment) {
        return new IntegrityAssessmentJpaEntity(
                assessment.getId(), assessment.getMatchId(), assessment.getScore(),
                assessment.getRiskLevel().name(), assessment.getAssessedAt()
        );
    }

    public List<IntegrityFactorJpaEntity> toJpaFactorEntities(IntegrityAssessment assessment) {
        return assessment.getFactors().stream()
                .map(f -> new IntegrityFactorJpaEntity(UUID.randomUUID(), assessment.getId(), f.code(),
                        f.description(), f.points())).toList();
    }
}
