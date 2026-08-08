package io.github.williamandradesantana.sports.application.league;

import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LeagueApplicationConfig {

    @Bean
    public SyncLeagueUseCase syncLeagueUseCase(
            LeagueRepository leagueRepository,
            SeasonRepository seasonRepository,
            LeagueProvider leagueProvider
    ) {
        return new SyncLeagueUseCase(leagueProvider, leagueRepository, seasonRepository);
    }
}
