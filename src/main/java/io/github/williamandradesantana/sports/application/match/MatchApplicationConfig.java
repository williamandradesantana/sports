package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.match.MatchStatisticsRepository;
import io.github.williamandradesantana.sports.domain.match.OddsRepository;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.TrackedBookmakersProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TrackedBookmakersProperties.class)
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

    @Bean
    public SyncMatchStatisticsUseCase syncMatchStatisticsUseCase(
            MatchStatisticsProvider matchStatisticsProvider, MatchStatisticsRepository matchStatisticsRepository,
            MatchRepository matchRepository, TeamRepository teamRepository
    ) {
        return new SyncMatchStatisticsUseCase(matchStatisticsProvider, matchStatisticsRepository, teamRepository, matchRepository);
    }

    @Bean
    public GetMatchStatisticsUseCase getMatchStatisticsUseCase(
            MatchRepository matchRepository, MatchStatisticsRepository matchStatisticsRepository
    ) {
        return new GetMatchStatisticsUseCase(matchRepository, matchStatisticsRepository);
    }

    @Bean
    public SyncOddsUseCase syncOddsUseCase(OddsProvider oddsProvider, OddsRepository oddsRepository,
                                           MatchRepository matchRepository,
                                           TrackedBookmakersProperties trackedBookmakersProperties) {
        return new SyncOddsUseCase(oddsProvider, oddsRepository, matchRepository, trackedBookmakersProperties);
    }
}
