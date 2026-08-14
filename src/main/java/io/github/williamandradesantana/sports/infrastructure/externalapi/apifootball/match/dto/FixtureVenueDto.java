package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureVenueDto(Long id, String name, String city) {
}
