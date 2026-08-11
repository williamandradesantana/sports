package io.github.williamandradesantana.sports.domain.venue;

import java.util.Optional;
import java.util.UUID;

public interface VenueRepository {
    Optional<Venue> findById(UUID id);
    Optional<Venue> findByExternalId(Long externalId);
    void save(Venue venue);
}
