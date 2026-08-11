package io.github.williamandradesantana.sports.interfaces.league.dto;

import io.github.williamandradesantana.sports.domain.league.League;

import java.util.UUID;

public record LeagueResponse(UUID id, Long externalId, String name, String type, String logoUrl, String countryName) {

    public static LeagueResponse from(League league) {
        return new LeagueResponse(
            league.getId(),
            league.getExternalId(),
            league.getName(),
            league.getType().name(),
            league.getLogoUrl(),
            league.getCountry().name()
        );
    }
}
