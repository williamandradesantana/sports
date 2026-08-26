package io.github.williamandradesantana.sports.domain.integrity;

import io.github.williamandradesantana.sports.domain.integrity.exceptions.InvalidIntegrityAssessmentException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class IntegrityAssessment {

    private final UUID id;
    private final UUID matchId;
    private final int score;
    private final RiskLevel riskLevel;
    private final List<IntegrityFactor> factors;
    private final OffsetDateTime assessedAt;

    public IntegrityAssessment(UUID id, UUID matchId, int score, List<IntegrityFactor> factors, OffsetDateTime assessedAt) {
        if (matchId == null) throw new InvalidIntegrityAssessmentException("Assessment must belong to a match");
        if (score < 0 || score > 100)
            throw new InvalidIntegrityAssessmentException("Score must be between 0 and 100");
        if (assessedAt == null) throw new InvalidIntegrityAssessmentException("AssessedAt cannot be null");
        this.id = id;
        this.matchId = matchId;
        this.score = score;
        this.riskLevel = resolveRiskLevel(score);
        this.factors = factors != null ? List.copyOf(factors) : List.of();
        this.assessedAt = assessedAt;
    }

    private static RiskLevel resolveRiskLevel(int score) {
        if (score >= 75) return RiskLevel.CRITICAL;
        if (score >= 50) return RiskLevel.HIGH;
        if (score >= 25) return RiskLevel.MEDIUM;
        return RiskLevel.LOW;
    }

    public UUID getId() {
        return id;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public int getScore() {
        return score;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public List<IntegrityFactor> getFactors() {
        return factors;
    }

    public OffsetDateTime getAssessedAt() {
        return assessedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        IntegrityAssessment that = (IntegrityAssessment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
