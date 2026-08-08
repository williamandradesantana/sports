package io.github.williamandradesantana.sports.infrastructure.persistence.league;

import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeaguePersistenceConfig {

    @Bean
    public LeagueMapper leagueMapper() {
        return new LeagueMapper();
    }

    @Bean
    public LeagueRepository leagueRepository(LeagueJpaRepository jpaRepository, LeagueMapper leagueMapper) {
        return new LeagueRepositoryImpl(jpaRepository, leagueMapper);
    }
}
