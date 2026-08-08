package io.github.williamandradesantana.sports.application.league;

import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.LeagueType;

import java.util.List;

public record ExternalLeagueData(
        Long externalId,
        String name,
        LeagueType type,
        String logoUrl,
        Country country,
        List<ExternalSeasonData> seasons
) {
}
