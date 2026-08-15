package io.github.williamandradesantana.sports.infrastructure.scheduling.match;

import io.github.williamandradesantana.sports.application.match.SyncMatchUseCase;
import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.TrackedLeaguesProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class MatchSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(MatchSyncScheduler.class);

    private final SyncMatchUseCase syncMatchUseCase;
    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;
    private final TrackedLeaguesProperties trackedLeaguesProperties;

    public MatchSyncScheduler(SyncMatchUseCase syncMatchUseCase, LeagueRepository leagueRepository,
                              SeasonRepository seasonRepository, TrackedLeaguesProperties trackedLeaguesProperties) {
        this.syncMatchUseCase = syncMatchUseCase;
        this.leagueRepository = leagueRepository;
        this.seasonRepository = seasonRepository;
        this.trackedLeaguesProperties = trackedLeaguesProperties;
    }

//    @Scheduled(cron = "${app.scheduling.match-sync-cron}")
    public void syncMatchesForTrackedLeagues() {
        log.info("Starting scheduled match sync for {} tracked league(s)",
                trackedLeaguesProperties.externalIds().size());

        for (Long leagueExternalId : trackedLeaguesProperties.externalIds()) {
            try {
                int currentSeasonYear = resolveCurrentSeasonYear(leagueExternalId);
                syncMatchUseCase.syncByLeagueAndSeason(leagueExternalId, currentSeasonYear);
                log.info("Successfully synced matches for league externalId={}, season={}",
                        leagueExternalId, currentSeasonYear);
            } catch (RuntimeException e) {
                log.error("Failed to sync matches for league externalId={}, skipping to next",
                        leagueExternalId, e);
            }
        }
    }

    private int resolveCurrentSeasonYear(Long leagueExternalId) {
        League league = leagueRepository.findByExternalId(leagueExternalId)
                .orElseThrow(() -> new IllegalStateException(
                        "League not synced yet, cannot sync matches: externalId=" + leagueExternalId));

        Season currentSeason = seasonRepository.findCurrentByLeagueId(league.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "No current season found for league externalId=" + leagueExternalId));

        return currentSeason.getYear();
    }
}
