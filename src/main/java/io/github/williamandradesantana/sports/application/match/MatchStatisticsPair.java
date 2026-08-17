package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.domain.match.MatchStatistics;

import java.util.Optional;

public record MatchStatisticsPair(Optional<MatchStatistics> home, Optional<MatchStatistics> away) {
}
