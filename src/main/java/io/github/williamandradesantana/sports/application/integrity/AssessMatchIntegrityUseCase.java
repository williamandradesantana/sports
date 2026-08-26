package io.github.williamandradesantana.sports.application.integrity;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessment;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessmentRepository;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityScoringService;
import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.match.MatchStatistics;
import io.github.williamandradesantana.sports.domain.match.MatchStatisticsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AssessMatchIntegrityUseCase {

    private final MatchRepository matchRepository;
    private final IntegrityAssessmentRepository integrityAssessmentRepository;
    private final IntegrityScoringService integrityScoringService;
    private final MatchStatisticsRepository matchStatisticsRepository;

    public AssessMatchIntegrityUseCase(MatchRepository matchRepository, IntegrityAssessmentRepository integrityAssessmentRepository, IntegrityScoringService integrityScoringService, MatchStatisticsRepository matchStatisticsRepository) {
        this.matchRepository = matchRepository;
        this.integrityAssessmentRepository = integrityAssessmentRepository;
        this.integrityScoringService = integrityScoringService;
        this.matchStatisticsRepository = matchStatisticsRepository;
    }

    public IntegrityAssessment execute(UUID matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + matchId));

        if (!match.isFinished())
            throw new MatchNotAssessableException("Match must be finished before its integrity can be assessed: " + matchId);

        List<MatchStatistics> statistics = matchStatisticsRepository.findByMatchId(matchId);

        MatchStatistics homeStats = findStatsForTeam(statistics, match.getHomeTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Home team statistics not synced yet for match: " + matchId));
        MatchStatistics awayStats = findStatsForTeam(statistics, match.getAwayTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Away team statistics not synced yet for match: " + matchId));

        IntegrityAssessment assessment = integrityScoringService.assess(match, homeStats, awayStats);
        integrityAssessmentRepository.save(assessment);
        return assessment;
    }

    private Optional<MatchStatistics> findStatsForTeam(List<MatchStatistics> statistics, UUID teamId) {
        return statistics.stream()
                .filter(stats -> stats.getTeamId().equals(teamId))
                .findFirst();
    }
}
