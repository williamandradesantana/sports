package io.github.williamandradesantana.sports.domain.match;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchStatisticsRepository {
    Optional<MatchStatistics> findByMatchIdAndTeamId(UUID matchId, UUID teamId);
    List<MatchStatistics> findByMatchId(UUID matchId);
    void save(MatchStatistics statistics);
}
