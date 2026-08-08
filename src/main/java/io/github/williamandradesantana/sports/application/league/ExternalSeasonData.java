package io.github.williamandradesantana.sports.application.league;

import java.time.LocalDate;

public record ExternalSeasonData(int year, LocalDate startDate, LocalDate endDate, boolean current) {
}
