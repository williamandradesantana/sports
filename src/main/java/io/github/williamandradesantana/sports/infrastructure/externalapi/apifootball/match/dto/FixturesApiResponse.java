package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixturesApiResponse(List<FixtureResponseItem> response) {
}
