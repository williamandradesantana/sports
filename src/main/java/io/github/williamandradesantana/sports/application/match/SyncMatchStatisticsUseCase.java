package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.match.MatchStatistics;
import io.github.williamandradesantana.sports.domain.match.MatchStatisticsRepository;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;

import java.util.List;
import java.util.UUID;

public class SyncMatchStatisticsUseCase {

    private final MatchStatisticsProvider matchStatisticsProvider;
    private final MatchStatisticsRepository matchStatisticsRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;

    public SyncMatchStatisticsUseCase(MatchStatisticsProvider matchStatisticsProvider, MatchStatisticsRepository matchStatisticsRepository, TeamRepository teamRepository, MatchRepository matchRepository) {
        this.matchStatisticsProvider = matchStatisticsProvider;
        this.matchStatisticsRepository = matchStatisticsRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
    }

    public List<MatchStatistics> syncByMatchExternalId(Long matchExternalId) {
        Match match = matchRepository.findByExternalId(matchExternalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Match not synced yet: externalId=" + matchExternalId));

        List<ExternalMatchStatisticsData> externalData = matchStatisticsProvider.fetchByMatchExternalId(matchExternalId);

        return externalData.stream().map(data -> syncStatistics(match.getId(), data)).toList();
    }

    private MatchStatistics syncStatistics(UUID matchId, ExternalMatchStatisticsData data) {
        Team team = teamRepository.findByExternalId(data.teamExternalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Team not synced yet: externalId=" + data.teamExternalId()));

        MatchStatistics stats = matchStatisticsRepository.findByMatchIdAndTeamId(matchId, team.getId())
                .map(existing -> updateExisting(existing, data))
                .orElseGet(() -> createNew(matchId, team.getId(), data));

        matchStatisticsRepository.save(stats);
        return stats;
    }

    private MatchStatistics updateExisting(MatchStatistics stats, ExternalMatchStatisticsData data) {
        stats.updateFromExternalSource(
            data.shotsOnGoal(), data.shotsOffGoal(), data.totalShots(), data.blockedShots(),
            data.shotsInsideBox(), data.shotsOutsideBox(), data.fouls(), data.cornerKicks(), data.offsides(),
            data.ballPossessionPercentage(), data.yellowCards(), data.redCards(), data.goalkeeperSaves(),
            data.totalPasses(), data.passesAccurate(), data.passesAccuracyPercentage()
        );
        return stats;
    }

    private MatchStatistics createNew(UUID matchId, UUID teamId, ExternalMatchStatisticsData data) {
        return new MatchStatistics(
            UUID.randomUUID(), matchId, teamId,
            data.shotsOnGoal(), data.shotsOffGoal(), data.totalShots(), data.blockedShots(),
            data.shotsInsideBox(), data.shotsOutsideBox(), data.fouls(), data.cornerKicks(), data.offsides(),
            data.ballPossessionPercentage(), data.yellowCards(), data.redCards(), data.goalkeeperSaves(),
            data.totalPasses(), data.passesAccurate(), data.passesAccuracyPercentage()
        );
    }
}
