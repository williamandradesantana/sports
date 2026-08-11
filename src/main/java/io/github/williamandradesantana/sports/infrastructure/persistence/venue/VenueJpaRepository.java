package io.github.williamandradesantana.sports.infrastructure.persistence.venue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VenueJpaRepository extends JpaRepository<VenueJpaEntity, UUID> {
    Optional<VenueJpaEntity> findByExternalId(Long externalId);
}
