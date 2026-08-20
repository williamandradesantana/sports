package io.github.williamandradesantana.sports.domain.competition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StandingRepository {
    List<Standing> findBySeasonId(UUID seasonId);
    Optional<Standing> findBySeasonIdAndTeamId(UUID seasonId, UUID teamId);
    void save(Standing standing);
}
