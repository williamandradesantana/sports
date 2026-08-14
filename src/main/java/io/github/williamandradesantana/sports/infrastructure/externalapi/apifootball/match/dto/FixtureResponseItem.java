package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureResponseItem(
        FixtureDto fixture, FixtureLeagueDto league, FixtureTeamsDto teams, FixtureGoalsDto goals
) {
}
