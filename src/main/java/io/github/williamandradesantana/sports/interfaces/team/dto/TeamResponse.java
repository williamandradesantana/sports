package io.github.williamandradesantana.sports.interfaces.team.dto;

import io.github.williamandradesantana.sports.domain.team.Team;

import java.util.UUID;

public record TeamResponse(
        UUID id, Long externalId, String name, String code, String countryName,
        Integer founded, boolean national, String logoUrl
) {
    public static TeamResponse from(Team team) {
        return new TeamResponse(
            team.getId(), team.getExternalId(), team.getName(), team.getCode(), team.getCountryName(),
            team.getFounded().orElse(null), team.isNational(), team.getLogoUrl()
        );
    }
}
