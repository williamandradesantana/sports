package io.github.williamandradesantana.sports.application.team;

public record ExternalTeamData(
        Long externalId, String name, String code, String countryName,
        Integer founded, boolean national, String logoUrl, ExternalVenueData venue
) {
}
