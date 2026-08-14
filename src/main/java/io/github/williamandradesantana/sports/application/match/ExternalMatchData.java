package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.domain.match.MatchStatus;

import java.time.OffsetDateTime;

public record ExternalMatchData(
        Long externalId, Long leagueExternalId, int seasonYear,
        Long homeTeamExternalId, Long awayTeamExternalId, Long venueExternalId,
        OffsetDateTime matchDate, MatchStatus status,
        Integer homeGoals, Integer awayGoals, String round, String referee
) {
}
