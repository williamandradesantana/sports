package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.match.MatchStatisticsRepository;
import io.github.williamandradesantana.sports.domain.match.OddsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MatchPersistenceConfig {

    @Bean
    public MatchMapper matchMapper() {
        return new MatchMapper();
    }

    @Bean
    public MatchRepository matchRepository(MatchJpaRepository jpaRepository, MatchMapper matchMapper) {
        return new MatchRepositoryImpl(jpaRepository, matchMapper);
    }

    @Bean
    public MatchStatisticsMapper matchStatisticsMapper() {
        return new MatchStatisticsMapper();
    }

    @Bean
    public MatchStatisticsRepository matchStatisticsRepository(
            MatchStatisticsJpaRepository jpaRepository, MatchStatisticsMapper matchStatisticsMapper
    ) {
        return new MatchStatisticsRepositoryImpl(jpaRepository, matchStatisticsMapper);
    }

    @Bean
    public OddsMapper oddsMapper() {
        return new OddsMapper();
    }

    @Bean
    public OddsRepository oddsRepository(OddsJpaRepository oddsJpaRepository, OddsMapper oddsMapper) {
        return new OddsRepositoryImpl(oddsJpaRepository, oddsMapper);
    }
}
