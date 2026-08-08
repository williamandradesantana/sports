package io.github.williamandradesantana.sports.infrastructure.shared.apifootball;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeagueDto(Long id, String name, String type, String logo) {
}
