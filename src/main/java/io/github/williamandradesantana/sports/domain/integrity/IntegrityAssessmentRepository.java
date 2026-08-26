package io.github.williamandradesantana.sports.domain.integrity;

import java.util.Optional;
import java.util.UUID;

public interface IntegrityAssessmentRepository {
    Optional<IntegrityAssessment> findLatestByMatchId(UUID matchId);
    void save(IntegrityAssessment assessment);
}
