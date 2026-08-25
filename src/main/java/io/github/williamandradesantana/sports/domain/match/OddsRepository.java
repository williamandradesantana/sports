package io.github.williamandradesantana.sports.domain.match;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OddsRepository {
    List<Odds> findByMatchId(UUID matchId);
    Optional<Odds> findLatestByMatchId(UUID matchId);
    void save(Odds odds);
}
