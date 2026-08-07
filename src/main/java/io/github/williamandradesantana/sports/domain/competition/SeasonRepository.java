package io.github.williamandradesantana.sports.domain.competition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeasonRepository {
    Optional<Season> findByLeagueIdAndYear(UUID leagueId, int year);
    Optional<Season> findByCurrentByLeagueId(UUID id);
    List<Season> findAllLeagueId(UUID leagueId);
    void save(Season season);
}
