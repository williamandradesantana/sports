package io.github.williamandradesantana.sports.infrastructure.scheduling;

import io.github.williamandradesantana.sports.application.league.SyncLeagueUseCase;
import io.github.williamandradesantana.sports.infrastructure.scheduling.league.LeagueSyncScheduler;
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
    public LeagueSyncScheduler leagueSyncScheduler(SyncLeagueUseCase syncLeagueUseCase, TrackedLeaguesProperties trackedLeaguesProperties) {
        return new LeagueSyncScheduler(syncLeagueUseCase, trackedLeaguesProperties);
    }
}
