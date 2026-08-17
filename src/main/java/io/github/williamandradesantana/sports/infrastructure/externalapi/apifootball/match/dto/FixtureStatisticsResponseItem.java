package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureStatisticsResponseItem(FixtureStatisticsTeamDto team, List<StatisticEntryDto> statistics) {
}
