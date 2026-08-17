package io.github.williamandradesantana.sports.interfaces.match.dto;

import io.github.williamandradesantana.sports.application.match.MatchDetails;
import io.github.williamandradesantana.sports.application.match.MatchStatisticsPair;
import io.github.williamandradesantana.sports.domain.venue.Venue;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MatchDetailResponse(
    UUID id, Long externalId, String status, OffsetDateTime matchDate,
    String round, String referee, TeamRefResponse homeTeam, TeamRefResponse awayTeam,
    Integer homeGoals, Integer awayGoals, String venueName,
    MatchStatisticsResponse homeStatistics, MatchStatisticsResponse awayStatistics
) {
    public static MatchDetailResponse from(MatchDetails details, MatchStatisticsPair statistics) {
        return new MatchDetailResponse(
            details.match().getId(), details.match().getExternalId(), details.match().getStatus().name(),
            details.match().getMatchDate(), details.match().getRound().orElse(null),
            details.match().getReferee().orElse(null),
            TeamRefResponse.from(details.homeTeam()), TeamRefResponse.from(details.awayTeam()),
            details.match().getHomeGoals().orElse(null), details.match().getAwayGoals().orElse(null),
            details.venue().map(Venue::getName).orElse(null),
            statistics.home().map(MatchStatisticsResponse::from).orElse(null),
            statistics.away().map(MatchStatisticsResponse::from).orElse(null)
        );
    }
}
