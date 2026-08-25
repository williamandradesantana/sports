package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.match.Odds;
import io.github.williamandradesantana.sports.domain.match.OddsRepository;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.TrackedBookmakersProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SyncOddsUseCase {

    private final OddsProvider oddsProvider;
    private final OddsRepository oddsRepository;
    private final MatchRepository matchRepository;
    private final TrackedBookmakersProperties trackedBookmakersProperties;

    public SyncOddsUseCase(OddsProvider oddsProvider, OddsRepository oddsRepository, MatchRepository matchRepository, TrackedBookmakersProperties trackedBookmakersProperties) {
        this.oddsProvider = oddsProvider;
        this.oddsRepository = oddsRepository;
        this.matchRepository = matchRepository;
        this.trackedBookmakersProperties = trackedBookmakersProperties;
    }

    public List<Odds> syncByMatchExternalId(Long matchExternalId) {
        Match match = matchRepository.findByExternalId(matchExternalId)
            .orElseThrow(() -> new ResourceNotFoundException("Match not synced yet: externalId=" + matchExternalId));

        List<Odds> synced = new ArrayList<>();
        for (Long bookmakerExternalId : trackedBookmakersProperties.externalIds()) {
            oddsProvider.fetchByMatchAndBookmaker(matchExternalId, bookmakerExternalId)
                    .ifPresent(data -> synced.add(createAndSave(match.getId(), data)));
        }
        return synced;
    }

    private Odds createAndSave(UUID matchId, ExternalOddsData data) {
        Odds odds = new Odds(
            UUID.randomUUID(), matchId, data.bookmakerExternalId(), data.bookmakerName(), data.capturedAt(),
            data.homeWinOdd(), data.drawOdd(), data.awayWinOdd(), data.overGoalsOdd(), data.underGoalsOdd(),
            data.bothTeamsScoreYesOdd(), data.bothTeamsScoreNoOdd()
        );
        oddsRepository.save(odds);
        return odds;
    }
}
