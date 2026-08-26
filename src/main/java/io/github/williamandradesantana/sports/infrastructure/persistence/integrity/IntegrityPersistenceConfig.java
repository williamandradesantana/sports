package io.github.williamandradesantana.sports.infrastructure.persistence.integrity;

import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrityPersistenceConfig {

    @Bean
    public IntegrityAssessmentMapper integrityAssessmentMapper() {
        return new IntegrityAssessmentMapper();
    }

    @Bean
    public IntegrityAssessmentRepository integrityAssessmentRepository(
            IntegrityAssessmentJpaRepository assessmentJpaRepository,
            IntegrityFactorJpaRepository integrityFactorJpaRepository,
            IntegrityAssessmentMapper mapper
    ) {
        return new IntegrityAssessmentRepositoryImpl(assessmentJpaRepository, integrityFactorJpaRepository, mapper);
    }
}
