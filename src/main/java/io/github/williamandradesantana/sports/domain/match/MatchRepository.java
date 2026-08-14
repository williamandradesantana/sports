package io.github.williamandradesantana.sports.domain.match;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepository {
    Optional<Match> findById(UUID id);
    Optional<Match> findByExternalId(Long externalId);
    List<Match> findBySeasonId(UUID seasonId);
    List<Match> findByTeamId(UUID teamId);
    void save(Match match);
}
