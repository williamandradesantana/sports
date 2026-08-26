package io.github.williamandradesantana.sports.application.integrity;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessment;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessmentRepository;

import java.util.UUID;

public class GetIntegrityAssessmentUseCase {

    private final IntegrityAssessmentRepository integrityAssessmentRepository;

    public GetIntegrityAssessmentUseCase(IntegrityAssessmentRepository integrityAssessmentRepository) {
        this.integrityAssessmentRepository = integrityAssessmentRepository;
    }

    public IntegrityAssessment execute(UUID matchId) {
        return integrityAssessmentRepository.findLatestByMatchId(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("No integrity assessment found for match: " + matchId));
    }
}
