package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StandingJpaRepository  extends JpaRepository<StandingJpaEntity, UUID> {
    List<StandingJpaEntity> findBySeasonId(UUID seasonId);
    Optional<StandingJpaEntity> findBySeasonIdAndTeamId(UUID seasonId, UUID teamId);
}
