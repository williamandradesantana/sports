package io.github.williamandradesantana.sports.infrastructure.scheduling.league;

import io.github.williamandradesantana.sports.application.league.SyncLeagueUseCase;
import io.github.williamandradesantana.sports.application.shared.ExternalDataSourceException;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.TrackedLeaguesProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class LeagueSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(LeagueSyncScheduler.class);

    private final SyncLeagueUseCase syncLeagueUseCase;
    private final TrackedLeaguesProperties trackedLeaguesProperties;

    public LeagueSyncScheduler(SyncLeagueUseCase syncLeagueUseCase, TrackedLeaguesProperties trackedLeaguesProperties) {
        this.syncLeagueUseCase = syncLeagueUseCase;
        this.trackedLeaguesProperties = trackedLeaguesProperties;
    }

//    @Scheduled(cron = "${app.scheduling.league-sync-cron}")
    public void syncTrackedLeagues() {
        log.info("Starting scheduled league sync for {} tracked leagues(s)", trackedLeaguesProperties.externalIds().size());

        for (Long externalId : trackedLeaguesProperties.externalIds()) {
            try {
                syncLeagueUseCase.syncByExternalId(externalId);
                log.info("Successfully synced league externalId={}", externalId);
            } catch (ExternalDataSourceException e) {
                log.error("Failed to sync league externalId={}, skipping to next", externalId, e);
            }
        }
    }
}
