package io.github.williamandradesantana.sports.application.team;

public record ExternalVenueData(
        Long externalId, String name, String address, String city,
        Integer capacity, String surface, String imageUrl
) {
}
