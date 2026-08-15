package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MatchApplicationConfig {

    @Bean
    public SyncMatchUseCase syncMatchUseCase(
        MatchProvider matchProvider, MatchRepository matchRepository,
        LeagueRepository leagueRepository, SeasonRepository seasonRepository,
        TeamRepository teamRepository, VenueRepository venueRepository
    ) {
        return new SyncMatchUseCase(
            matchProvider, matchRepository, leagueRepository,
            seasonRepository, teamRepository, venueRepository
        );
    }

    @Bean
    public GetMatchDetailsUseCase getMatchDetailsUseCase(
            TeamRepository teamRepository, VenueRepository venueRepository, MatchRepository matchRepository
    ) {
        return new GetMatchDetailsUseCase(teamRepository, venueRepository, matchRepository);
    }

    @Bean
    public GetMatchesByTeamUseCase getMatchesByTeamUseCase(
            MatchRepository matchRepository, GetMatchDetailsUseCase getMatchDetailsUseCase
    ) {
        return new GetMatchesByTeamUseCase(matchRepository, getMatchDetailsUseCase);
    }
}
