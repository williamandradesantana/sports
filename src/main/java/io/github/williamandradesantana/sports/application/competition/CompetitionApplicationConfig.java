package io.github.williamandradesantana.sports.application.competition;

import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.competition.StandingRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompetitionApplicationConfig {

    @Bean
    public SyncStandingsUseCase syncStandingUseCase(
            StandingProvider standingProvider, StandingRepository standingRepository,
            LeagueRepository leagueRepository, SeasonRepository seasonRepository, TeamRepository teamRepository
    ) {
        return new SyncStandingsUseCase(
                standingProvider, standingRepository, teamRepository, seasonRepository, leagueRepository
        );
    }

    @Bean
    public GetStandingsUseCase getStandingsUseCase(StandingRepository standingRepository, TeamRepository teamRepository) {
        return new GetStandingsUseCase(standingRepository, teamRepository);
    }
}
