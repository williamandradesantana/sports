package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchJpaRepository extends JpaRepository<MatchJpaEntity, UUID> {
    Optional<MatchJpaEntity> findByExternalId(Long externalId);
    List<MatchJpaEntity> findBySeasonId(UUID seasonId);
    List<MatchJpaEntity> findByHomeTeamIdOrAwayTeamId(UUID homeTeamId, UUID awayTeamId);
}
