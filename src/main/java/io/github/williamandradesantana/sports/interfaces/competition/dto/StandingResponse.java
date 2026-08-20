package io.github.williamandradesantana.sports.interfaces.competition.dto;

import io.github.williamandradesantana.sports.domain.competition.Standing;
import io.github.williamandradesantana.sports.domain.team.Team;

import java.time.OffsetDateTime;
import java.util.UUID;

public record StandingResponse(
        int rank, UUID teamId, String teamName, String teamLogoUrl, int points,
        int goalsDifference, String groupName, String form, String trend,
        String description, StandingRecordResponse overall, StandingRecordResponse home,
        StandingRecordResponse away, OffsetDateTime lastUpdatedAt
) {
    public static StandingResponse from(Standing standing, Team team) {
        return new StandingResponse(
            standing.getRank(), team.getId(), team.getName(), team.getLogoUrl(), standing.getPoints(),
            standing.getOverall().goalDifference(), standing.getGroupName(),
            standing.getForm().orElse(null), standing.getTrend().name(), standing.getDescription().orElse(null),
            StandingRecordResponse.from(standing.getOverall()), StandingRecordResponse.from(standing.getHome()),
            StandingRecordResponse.from(standing.getAway()), standing.getLastUpdatedAt()
        );
    }
}
