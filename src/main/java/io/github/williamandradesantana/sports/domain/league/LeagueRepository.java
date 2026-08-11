package io.github.williamandradesantana.sports.domain.league;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeagueRepository {
    Optional<League> findById(UUID id);
    Optional<League> findByExternalId(Long externalId);
    Page<League> findAll(Pageable pageable);
    void save(League league);
}
