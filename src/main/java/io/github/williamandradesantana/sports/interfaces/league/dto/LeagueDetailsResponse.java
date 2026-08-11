package io.github.williamandradesantana.sports.interfaces.league.dto;

import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.interfaces.competition.dto.SeasonResponse;

import java.util.List;
import java.util.UUID;

public record LeagueDetailsResponse(UUID id, Long externalId, String name, String type,
                                    String logoUrl, String countryName, List<SeasonResponse> seasons) {
    public static LeagueDetailsResponse from(League league, List<SeasonResponse> seasons) {
        return new LeagueDetailsResponse(
            league.getId(),
            league.getExternalId(),
            league.getName(),
            league.getType().name(),
            league.getLogoUrl(),
            league.getCountry().name(),
            seasons
        );
    }
}
