package io.github.williamandradesantana.sports.infrastructure.persistence.league;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LeagueJpaRepository extends JpaRepository<LeagueJpaEntity, UUID> {
    Optional<LeagueJpaEntity> findByExternalId(Long externalId);
}
