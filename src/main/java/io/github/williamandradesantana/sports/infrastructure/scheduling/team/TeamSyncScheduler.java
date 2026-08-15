package io.github.williamandradesantana.sports.infrastructure.scheduling.team;

import io.github.williamandradesantana.sports.application.team.SyncTeamUseCase;
import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.TrackedLeaguesProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class TeamSyncScheduler {

    private static final Logger logger = LoggerFactory.getLogger(TeamSyncScheduler.class);

    private final SyncTeamUseCase syncTeamUseCase;
    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;
    private final TrackedLeaguesProperties trackedLeaguesProperties;

    public TeamSyncScheduler(SyncTeamUseCase syncTeamUseCase, LeagueRepository leagueRepository, SeasonRepository seasonRepository, TrackedLeaguesProperties trackedLeaguesProperties) {
        this.syncTeamUseCase = syncTeamUseCase;
        this.leagueRepository = leagueRepository;
        this.seasonRepository = seasonRepository;
        this.trackedLeaguesProperties = trackedLeaguesProperties;
    }

//    @Scheduled(cron = "${app.scheduling.team-sync-cron}")
    public void syncTeamsForTrackedLeagues() {
        logger.info("Starting scheduled team sync for {} tracked league(s)",
                trackedLeaguesProperties.externalIds().size());

        for (Long leagueExternalId : trackedLeaguesProperties.externalIds()) {
            try {
                int currentSeasonYear = resolveCurrentSeasonYear(leagueExternalId);
                syncTeamUseCase.syncByLeagueAndSeason(leagueExternalId, currentSeasonYear);
                logger.info("Successfully synced teams for league externalId={}, season={}",
                        leagueExternalId, currentSeasonYear);
            } catch (RuntimeException e) {
                logger.error("Failed to sync teams for league externalId={}, skipping to next",
                        leagueExternalId, e);
            }
        }
    }

    private int resolveCurrentSeasonYear(Long leagueExternalId) {
        League league = leagueRepository.findByExternalId(leagueExternalId)
                .orElseThrow(() -> new IllegalStateException(
                        "League not synced yet, cannot sync teams: externalId=" + leagueExternalId));

        Season currentSeason = seasonRepository.findCurrentByLeagueId(league.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "No current season found for league externalId=" + leagueExternalId));

        return currentSeason.getYear();
    }
}
