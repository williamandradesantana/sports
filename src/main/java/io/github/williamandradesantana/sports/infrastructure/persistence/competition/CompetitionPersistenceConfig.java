package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.competition.StandingRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompetitionPersistenceConfig {

    @Bean
    public SeasonMapper seasonMapper() {
        return new SeasonMapper();
    }

    @Bean
    public SeasonRepository seasonRepository(SeasonJpaRepository jpaRepository, SeasonMapper seasonMapper) {
        return new SeasonRepositoryImpl(jpaRepository, seasonMapper);
    }

    @Bean
    public StandingMapper standingMapper() {
        return new StandingMapper();
    }

    @Bean
    public StandingRepository standingRepository(StandingJpaRepository jpaRepository, StandingMapper mapper) {
        return new StandingRepositoryImpl(jpaRepository, mapper);
    }
}
