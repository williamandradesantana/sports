package io.github.williamandradesantana.sports.infrastructure.persistence.integrity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "tb_integrity_factors")
public class IntegrityFactorJpaEntity {

    @Id
    private UUID id;

    @Column(name = "assessment_id", nullable = false)
    private UUID assessmentId;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "points", nullable = false)
    private int points;

    protected IntegrityFactorJpaEntity() {}

    public IntegrityFactorJpaEntity(UUID id, UUID assessmentId, String code, String description, int points) {
        this.id = id;
        this.assessmentId = assessmentId;
        this.code = code;
        this.description = description;
        this.points = points;
    }

    public UUID getId() { return id; }
    public UUID getAssessmentId() { return assessmentId; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public int getPoints() { return points; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegrityFactorJpaEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}