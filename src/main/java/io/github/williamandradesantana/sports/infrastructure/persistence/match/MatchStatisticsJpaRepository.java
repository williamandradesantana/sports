package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchStatisticsJpaRepository extends JpaRepository<MatchStatisticsJpaEntity, UUID> {
    Optional<MatchStatisticsJpaEntity> findByMatchIdAndTeamId(UUID matchId, UUID teamId);
    List<MatchStatisticsJpaEntity> findByMatchId(UUID matchId);
}
