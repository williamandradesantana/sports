package io.github.williamandradesantana.sports.domain.league;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeagueRepository {
    Optional<League> findById(UUID id);
    Optional<League> findByExternalId(Long id);
    List<League> finAll();
    void save(League league);
}
