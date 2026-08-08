package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeasonJpaRepository extends JpaRepository<SeasonJpaEntity, UUID> {
    Optional<SeasonJpaEntity> findByLeagueIdAndYear(UUID leagueId, int year);
    Optional<SeasonJpaEntity> findByLeagueIdAndCurrentTrue(UUID leagueId);
    List<SeasonJpaEntity> findAllByLeagueId(UUID leagueId);
}
