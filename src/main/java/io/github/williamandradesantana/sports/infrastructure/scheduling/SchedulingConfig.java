package io.github.williamandradesantana.sports.infrastructure.scheduling;

import io.github.williamandradesantana.sports.application.league.SyncLeagueUseCase;
import io.github.williamandradesantana.sports.application.match.SyncMatchUseCase;
import io.github.williamandradesantana.sports.application.team.SyncTeamUseCase;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.infrastructure.scheduling.league.LeagueSyncScheduler;
import io.github.williamandradesantana.sports.infrastructure.scheduling.match.MatchSyncScheduler;
import io.github.williamandradesantana.sports.infrastructure.scheduling.team.TeamSyncScheduler;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.TrackedLeaguesProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(TrackedLeaguesProperties.class)
public class SchedulingConfig {

    @Bean
    public TeamSyncScheduler teamSyncScheduler(
            SyncTeamUseCase syncTeamUseCase, LeagueRepository leagueRepository,
            SeasonRepository seasonRepository, TrackedLeaguesProperties trackedLeaguesProperties
    ) {
        return new TeamSyncScheduler(syncTeamUseCase,leagueRepository, seasonRepository, trackedLeaguesProperties);
    }

    @Bean
    public LeagueSyncScheduler leagueSyncScheduler(SyncLeagueUseCase syncLeagueUseCase, TrackedLeaguesProperties trackedLeaguesProperties) {
        return new LeagueSyncScheduler(syncLeagueUseCase, trackedLeaguesProperties);
    }

    @Bean
    public MatchSyncScheduler matchSyncScheduler(
            SyncMatchUseCase syncMatchUseCase, LeagueRepository leagueRepository,
            SeasonRepository seasonRepository, TrackedLeaguesProperties trackedLeaguesProperties
    ) {
        return new MatchSyncScheduler(syncMatchUseCase, leagueRepository, seasonRepository, trackedLeaguesProperties);
    }
}
