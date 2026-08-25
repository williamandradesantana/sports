package io.github.williamandradesantana.sports.interfaces.match.dto;

import io.github.williamandradesantana.sports.domain.match.Odds;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record OddsResponse(
        String bookmakerName, OffsetDateTime capturedAt, BigDecimal homeWinOdd,
        BigDecimal drawOdd, BigDecimal awayWinOdd, BigDecimal overGoalsOdd,
        BigDecimal underGoalsOdd, BigDecimal bothTeamsScoreYesOdd,
        BigDecimal bothTeamsScoreNoOdd) {
    public static OddsResponse from(Odds odds) {
        return new OddsResponse(
            odds.getBookmakerName(), odds.getCapturedAt(), odds.getHomeWinOdd().orElse(null),
            odds.getDrawOdd().orElse(null), odds.getAwayWinOdd().orElse(null),
            odds.getOverGoalsOdd().orElse(null), odds.getUnderGoalsOdd().orElse(null),
            odds.getBothTeamsScoreYesOdd().orElse(null), odds.getBothTeamsScoreNoOdd().orElse(null)
        );
    }
}
