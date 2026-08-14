package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureLeagueDto(Long id, String name, String country, int season, String round) {
}
