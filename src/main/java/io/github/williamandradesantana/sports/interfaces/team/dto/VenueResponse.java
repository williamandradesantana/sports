package io.github.williamandradesantana.sports.interfaces.team.dto;

import io.github.williamandradesantana.sports.domain.venue.Venue;

import java.util.UUID;

public record VenueResponse(
        UUID id, String name, String address, String city,
        Integer capacity, String surface, String imageUrl
) {
    public static VenueResponse from(Venue venue) {
        return new VenueResponse(
            venue.getId(), venue.getName(), venue.getAddress(), venue.getCity(),
            venue.getCapacity(), venue.getSurface(), venue.getImage()
        );
    }
}
