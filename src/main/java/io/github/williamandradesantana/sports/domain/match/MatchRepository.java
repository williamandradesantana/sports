package io.github.williamandradesantana.sports.domain.match;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepository {
    Optional<Match> findById(UUID id);
    Optional<Match> findByExternalId(Long externalId);
    List<Match> findBySeasonId(UUID seasonId);
    Page<Match> findByTeamId(Pageable pageable, UUID teamId);
    void save(Match match);
}
