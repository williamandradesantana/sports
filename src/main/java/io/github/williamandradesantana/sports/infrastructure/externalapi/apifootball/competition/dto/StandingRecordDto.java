package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StandingRecordDto(int played, int win, int draw, int lose, StandingGoalsDto goals) {
}
