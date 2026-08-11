package io.github.williamandradesantana.sports.infrastructure.persistence.venue;

import io.github.williamandradesantana.sports.domain.venue.Venue;

public class VenueMapper {

    public Venue toDomain(VenueJpaEntity entity) {
        return new Venue(
            entity.getId(),
            entity.getExternalId(),
            entity.getName(),
            entity.getAddress(),
            entity.getCity(),
            entity.getCapacity(),
            entity.getSurface(),
            entity.getImageUrl()
        );
    }

    public VenueJpaEntity toJpaEntity(Venue venue) {
        return new VenueJpaEntity(
            venue.getId(),
            venue.getExternalId(),
            venue.getName(),
            venue.getAddress(),
            venue.getCity(),
            venue.getCapacity(),
            venue.getSurface(),
            venue.getImage()
        );
    }
}
