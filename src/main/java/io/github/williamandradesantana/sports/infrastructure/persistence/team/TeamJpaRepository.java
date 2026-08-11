package io.github.williamandradesantana.sports.infrastructure.persistence.team;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TeamJpaRepository extends JpaRepository<TeamJpaEntity, UUID> {
    Optional<TeamJpaEntity> findByExternalId(Long externalId);
}
