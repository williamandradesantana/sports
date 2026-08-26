package io.github.williamandradesantana.sports.infrastructure.persistence.integrity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IntegrityFactorJpaRepository extends JpaRepository<IntegrityFactorJpaEntity, UUID> {
    List<IntegrityFactorJpaEntity> findByAssessmentId(UUID assessmentId);
}
