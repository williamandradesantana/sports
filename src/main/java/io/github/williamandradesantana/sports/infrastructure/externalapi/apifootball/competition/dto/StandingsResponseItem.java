package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StandingsResponseItem(StandingsLeagueDto league) {
}
