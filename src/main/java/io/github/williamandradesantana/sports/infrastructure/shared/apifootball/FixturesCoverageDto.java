package io.github.williamandradesantana.sports.infrastructure.shared.apifootball;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FixturesCoverageDto(
        boolean events,
        boolean lineups,
        @JsonProperty("statistics_fixtures") boolean statisticsFixtures,
        @JsonProperty("statistics_players") boolean statisticsPlayers
    ) {
}
