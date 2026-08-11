package io.github.williamandradesantana.sports.interfaces.competition.dto;

import io.github.williamandradesantana.sports.domain.competition.Season;

import java.time.LocalDate;
import java.util.UUID;

public record SeasonResponse(UUID id, UUID externalId, int year, LocalDate startDate, LocalDate endDate, boolean current) {
    public static SeasonResponse from(Season season) {
        return new SeasonResponse(
            season.getId(),
            season.getLeagueId(),
            season.getYear(),
            season.getStartDate(),
            season.getEndDate(),
            season.isCurrent()
        );
    }
}
