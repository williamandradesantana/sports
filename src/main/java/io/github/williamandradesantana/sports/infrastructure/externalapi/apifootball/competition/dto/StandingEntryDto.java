package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StandingEntryDto(
        int rank, StandingTeamDto team, int points, int goalsDiff, String group,
        String form, String status, String description, StandingRecordDto all,
        StandingRecordDto home, StandingRecordDto away, OffsetDateTime update
) {
}
