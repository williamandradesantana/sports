package io.github.williamandradesantana.sports.infrastructure.scheduling.match;

import io.github.williamandradesantana.sports.application.match.SyncOddsUseCase;
import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.OffsetDateTime;
import java.util.List;

public class OddsSyncScheduler {

    private static final Logger log = LoggerFactory.getLogger(OddsSyncScheduler.class);

    private final SyncOddsUseCase syncOddsUseCase;
    private final MatchRepository matchRepository;
    private final int captureWindowHours;

    public OddsSyncScheduler(SyncOddsUseCase syncOddsUseCase, MatchRepository matchRepository,
                             @Value("${app.odds.capture-window-hours}") int captureWindowHours) {
        this.syncOddsUseCase = syncOddsUseCase;
        this.matchRepository = matchRepository;
        this.captureWindowHours = captureWindowHours;
    }

    @Scheduled(cron = "${app.scheduling.odds-sync-cron}")
    public void syncOddsForUpcomingMatches() {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime windowEnd = now.plusHours(captureWindowHours);

        List<Match> upcomingMatches = matchRepository.findScheduledBetween(now, windowEnd);

        log.info("Starting scheduled odds sync for {} upcoming match(es) within {}h window",
                upcomingMatches.size(), captureWindowHours);

        for (Match match : upcomingMatches) {
            try {
                syncOddsUseCase.syncByMatchExternalId(match.getExternalId());
            } catch (RuntimeException e) {
                log.error("Failed to sync odds for match externalId={}, skipping to next",
                        match.getExternalId(), e);
            }
        }
    }
}
