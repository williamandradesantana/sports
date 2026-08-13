package io.github.williamandradesantana.sports.interfaces.team.dto;

import io.github.williamandradesantana.sports.application.team.TeamDetails;

import java.util.UUID;

public record TeamDetailResponse(
        UUID id, Long externalId, String name, String code, String countryName,
        Integer founded, boolean national, String logoUrl, VenueResponse venue
) {
    public static TeamDetailResponse from(TeamDetails details) {
        var team = details.team();
        VenueResponse venueResponse = details.venue().map(VenueResponse::from).orElse(null);

        return new TeamDetailResponse(
                team.getId(), team.getExternalId(), team.getName(), team.getCode(),
                team.getCountryName(), team.getFounded().orElse(null), team.isNational(),
                team.getLogoUrl(), venueResponse
        );
    }
}
