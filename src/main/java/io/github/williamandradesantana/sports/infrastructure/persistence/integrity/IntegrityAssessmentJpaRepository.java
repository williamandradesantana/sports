package io.github.williamandradesantana.sports.infrastructure.persistence.integrity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IntegrityAssessmentJpaRepository extends JpaRepository<IntegrityAssessmentJpaEntity, UUID> {
    IntegrityAssessmentJpaEntity findFirstByMatchIdOrderByAssessedAtDesc(UUID matchId);
}
