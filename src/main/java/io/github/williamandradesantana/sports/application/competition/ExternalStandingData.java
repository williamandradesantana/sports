package io.github.williamandradesantana.sports.application.competition;

import io.github.williamandradesantana.sports.domain.competition.StandingRecord;
import io.github.williamandradesantana.sports.domain.competition.StandingTrend;

import java.time.OffsetDateTime;

public record ExternalStandingData(
    Long teamExternalId, int rank, int points, String groupName, String form,
    StandingTrend trend, String description, StandingRecord overall,
    StandingRecord home, StandingRecord away, OffsetDateTime lastUpdatedAt)
{
}