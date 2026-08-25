package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchJpaRepository extends JpaRepository<MatchJpaEntity, UUID> {
    Optional<MatchJpaEntity> findByExternalId(Long externalId);
    List<MatchJpaEntity> findBySeasonId(UUID seasonId);
    Page<MatchJpaEntity> findByHomeTeamIdOrAwayTeamId(Pageable pageable, UUID homeTeamId, UUID awayTeamId);

    @Query("""
        select m from MatchJpaEntity m
        where m.status = 'SCHEDULED' and m.matchDate between :start and :end
    """)
    List<MatchJpaEntity> findScheduledBetween(OffsetDateTime start, OffsetDateTime end);
}
