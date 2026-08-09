package io.github.williamandradesantana.sports.interfaces.league.dto;

import java.util.UUID;

public record LeagueResponse(UUID id, Long externalId, String name, String type, String logoUrl, String countryName) {
}
