package io.github.williamandradesantana.sports.infrastructure.shared.apifootball;

import java.time.LocalDate;

public record SeasonDto(int year, LocalDate start, LocalDate end, boolean current, CoverageDto coverage) {
}
