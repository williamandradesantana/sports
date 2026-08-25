package io.github.williamandradesantana.sports.domain.match;

import io.github.williamandradesantana.sports.domain.match.exceptions.InvalidOddsException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Odds {

    private final UUID id;
    private final UUID matchId;
    private final Long bookmakerExternalId;
    private final String bookmakerName;
    private final OffsetDateTime capturedAt;
    private final BigDecimal homeWinOdd;
    private final BigDecimal drawOdd;
    private final BigDecimal awayWinOdd;
    private final BigDecimal overGoalsOdd;
    private final BigDecimal underGoalsOdd;
    private final BigDecimal bothTeamsScoreYesOdd;
    private final BigDecimal bothTeamsScoreNoOdd;

    public Odds(UUID id, UUID matchId, Long bookmakerExternalId, String bookmakerName, OffsetDateTime capturedAt, BigDecimal homeWinOdd, BigDecimal drawOdd, BigDecimal awayWinOdd, BigDecimal overGoalsOdd, BigDecimal underGoalsOdd, BigDecimal bothTeamsScoreYesOdd, BigDecimal bothTeamsScoreNoOdd) {
        if (matchId == null) throw new InvalidOddsException("Odds must belong to a match");
        if (bookmakerExternalId == null || bookmakerExternalId <= 0)
            throw new InvalidOddsException("Bookmarker external id must be a positive number");
        if (capturedAt == null) throw new InvalidOddsException("CapturedAt cannot be null");

        validatePositiveIfPresent(homeWinOdd, "homeWinOdd");
        validatePositiveIfPresent(drawOdd, "drawOdd");
        validatePositiveIfPresent(awayWinOdd, "awayWinOdd");
        validatePositiveIfPresent(overGoalsOdd, "overGoalsOdd");
        validatePositiveIfPresent(underGoalsOdd, "underGoalsOdd");
        validatePositiveIfPresent(bothTeamsScoreYesOdd, "bothTeamsScoreYesOdd");
        validatePositiveIfPresent(bothTeamsScoreNoOdd, "bothTeamsScoreNoOdd");

        this.id = id;
        this.matchId = matchId;
        this.bookmakerExternalId = bookmakerExternalId;
        this.bookmakerName = bookmakerName;
        this.capturedAt = capturedAt;
        this.homeWinOdd = homeWinOdd;
        this.drawOdd = drawOdd;
        this.awayWinOdd = awayWinOdd;
        this.overGoalsOdd = overGoalsOdd;
        this.underGoalsOdd = underGoalsOdd;
        this.bothTeamsScoreYesOdd = bothTeamsScoreYesOdd;
        this.bothTeamsScoreNoOdd = bothTeamsScoreNoOdd;
    }

    private void validatePositiveIfPresent(BigDecimal value, String fieldName) {
        if(value != null && value.compareTo(BigDecimal.ONE) <= 0)
            throw new InvalidOddsException(fieldName + " must be greater than 1.00 when informed");
    }

    public UUID getId() {
        return id;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public Long getBookmakerExternalId() {
        return bookmakerExternalId;
    }

    public String getBookmakerName() {
        return bookmakerName;
    }

    public OffsetDateTime getCapturedAt() {
        return capturedAt;
    }

    public Optional<BigDecimal> getHomeWinOdd() {
        return Optional.ofNullable(homeWinOdd);
    }

    public Optional<BigDecimal> getDrawOdd() {
        return Optional.ofNullable(drawOdd);
    }

    public Optional<BigDecimal> getAwayWinOdd() {
        return Optional.ofNullable(awayWinOdd);
    }

    public Optional<BigDecimal> getOverGoalsOdd() {
        return Optional.ofNullable(overGoalsOdd);
    }

    public Optional<BigDecimal> getUnderGoalsOdd() {
        return Optional.ofNullable(underGoalsOdd);
    }

    public Optional<BigDecimal> getBothTeamsScoreYesOdd() {
        return Optional.ofNullable(bothTeamsScoreYesOdd);
    }

    public Optional<BigDecimal> getBothTeamsScoreNoOdd() {
        return Optional.ofNullable(bothTeamsScoreNoOdd);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Odds odds = (Odds) o;
        return Objects.equals(id, odds.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
