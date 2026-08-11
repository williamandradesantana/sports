package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.team.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TeamDto(
        Long id, String name, String code, String country,
        Integer founded, boolean national, String logo
) {
}
