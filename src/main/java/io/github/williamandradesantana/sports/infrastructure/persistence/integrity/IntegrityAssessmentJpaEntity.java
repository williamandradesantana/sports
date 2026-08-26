package io.github.williamandradesantana.sports.infrastructure.persistence.integrity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_integrity_assessments")
public class IntegrityAssessmentJpaEntity {

    @Id
    private UUID id;

    @Column(name = "match_id", nullable = false)
    private UUID matchId;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "risk_level", nullable = false)
    private String riskLevel;

    @Column(name = "assessed_at", nullable = false)
    private OffsetDateTime assessedAt;

    protected IntegrityAssessmentJpaEntity() {}

    public IntegrityAssessmentJpaEntity(UUID id, UUID matchId, int score, String riskLevel,
                                        OffsetDateTime assessedAt) {
        this.id = id;
        this.matchId = matchId;
        this.score = score;
        this.riskLevel = riskLevel;
        this.assessedAt = assessedAt;
    }

    public UUID getId() { return id; }
    public UUID getMatchId() { return matchId; }
    public int getScore() { return score; }
    public String getRiskLevel() { return riskLevel; }
    public OffsetDateTime getAssessedAt() { return assessedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegrityAssessmentJpaEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
