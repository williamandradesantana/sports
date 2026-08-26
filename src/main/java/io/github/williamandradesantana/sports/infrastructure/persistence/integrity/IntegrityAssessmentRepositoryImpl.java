package io.github.williamandradesantana.sports.infrastructure.persistence.integrity;

import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessment;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessmentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class IntegrityAssessmentRepositoryImpl implements IntegrityAssessmentRepository {

    private final IntegrityAssessmentJpaRepository assessmentJpaRepository;
    private final IntegrityFactorJpaRepository factorJpaRepository;
    private final IntegrityAssessmentMapper mapper;

    public IntegrityAssessmentRepositoryImpl(IntegrityAssessmentJpaRepository assessmentJpaRepository, IntegrityFactorJpaRepository factorJpaRepository, IntegrityAssessmentMapper mapper) {
        this.assessmentJpaRepository = assessmentJpaRepository;
        this.factorJpaRepository = factorJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<IntegrityAssessment> findLatestByMatchId(UUID matchId) {
        IntegrityAssessmentJpaEntity entity = assessmentJpaRepository.findFirstByMatchIdOrderByAssessedAtDesc(matchId);
        if (entity == null) return Optional.empty();
        List<IntegrityFactorJpaEntity> factors = factorJpaRepository.findByAssessmentId(entity.getId());
        return Optional.of(mapper.toDomain(entity, factors));
    }

    @Override
    public void save(IntegrityAssessment assessment) {
        assessmentJpaRepository.save(mapper.toJpaEntity(assessment));
        List<IntegrityFactorJpaEntity> factorEntities = mapper.toJpaFactorEntities(assessment);
        if (!factorEntities.isEmpty()) factorJpaRepository.saveAll(factorEntities);
    }
}
