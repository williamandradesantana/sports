package io.github.williamandradesantana.sports.application.integrity;

import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessmentRepository;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityScoringService;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.match.MatchStatisticsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrityApplicationConfig {

    @Bean
    public IntegrityScoringService integrityScoringService() {
        return new IntegrityScoringService();
    }

    @Bean
    public AssessMatchIntegrityUseCase assessMatchIntegrityUseCase(
            MatchRepository matchRepository, IntegrityAssessmentRepository integrityAssessmentRepository,
            IntegrityScoringService integrityScoringService, MatchStatisticsRepository matchStatisticsRepository) {
        return new AssessMatchIntegrityUseCase(
                matchRepository, integrityAssessmentRepository, integrityScoringService, matchStatisticsRepository);
    }

    @Bean
    public GetIntegrityAssessmentUseCase getIntegrityAssessUseCase(IntegrityAssessmentRepository integrityAssessmentRepository) {
        return new GetIntegrityAssessmentUseCase(integrityAssessmentRepository);
    }
}
