package io.github.williamandradesantana.sports.infrastructure.shared.apifootball;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoverageDto(
        FixturesCoverageDto fixtures,
        boolean standings,
        boolean players,
        @JsonProperty("top_scorers") boolean topScorers,
        @JsonProperty("top_assists") boolean topAssists,
        @JsonProperty("top_cards") boolean topCards,
        boolean injuries,
        boolean predictions,
        boolean odds
        ) {
}
