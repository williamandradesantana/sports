package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.match.MatchStatistics;
import io.github.williamandradesantana.sports.domain.match.MatchStatisticsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GetMatchStatisticsUseCase {

    private final MatchRepository matchRepository;
    private final MatchStatisticsRepository matchStatisticsRepository;

    public GetMatchStatisticsUseCase(MatchRepository matchRepository, MatchStatisticsRepository matchStatisticsRepository) {
        this.matchRepository = matchRepository;
        this.matchStatisticsRepository = matchStatisticsRepository;
    }

    public MatchStatisticsPair execute(UUID matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + matchId));

        List<MatchStatistics> statistics = matchStatisticsRepository.findByMatchId(match.getId());

        Optional<MatchStatistics> home = statistics.stream()
                .filter(stat -> stat.getTeamId().equals(match.getHomeTeamId()))
                .findFirst();

        Optional<MatchStatistics> away = statistics.stream()
                .filter(stat -> stat.getTeamId().equals(match.getAwayTeamId()))
                .findFirst();
        return new MatchStatisticsPair(home, away);
    }
}
