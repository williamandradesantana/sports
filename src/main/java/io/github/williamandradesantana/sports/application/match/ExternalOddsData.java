package io.github.williamandradesantana.sports.application.match;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ExternalOddsData(
        Long bookmakerExternalId, String bookmakerName, OffsetDateTime capturedAt,
        BigDecimal homeWinOdd, BigDecimal drawOdd, BigDecimal awayWinOdd,
        BigDecimal overGoalsOdd, BigDecimal underGoalsOdd,
        BigDecimal bothTeamsScoreYesOdd, BigDecimal bothTeamsScoreNoOdd) {
}
