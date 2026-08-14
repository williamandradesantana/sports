package io.github.williamandradesantana.sports.domain.match;

import io.github.williamandradesantana.sports.domain.match.exceptions.InvalidMatchException;
import io.github.williamandradesantana.sports.domain.shared.exceptions.InvalidExternalIdException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    private UUID leagueId;
    private UUID seasonId;
    private UUID homeTeamId;
    private UUID awayTeamId;
    private OffsetDateTime matchDate;

    private Match match;

    @BeforeEach
    void setUp() {
        leagueId = UUID.randomUUID();
        seasonId = UUID.randomUUID();
        homeTeamId = UUID.randomUUID();
        awayTeamId = UUID.randomUUID();
        matchDate = OffsetDateTime.parse("2026-08-21T14:00:00Z");
        match = new Match(
            UUID.randomUUID(), 215662L, leagueId, seasonId, homeTeamId, awayTeamId,
            null, matchDate, MatchStatus.SCHEDULED, null, null,
            "Regular Season - 10", null
        );
    }

    @AfterEach
    void afterEach() {
        leagueId = null;
        seasonId = null;
        homeTeamId = null;
        awayTeamId = null;
        matchDate = null;
        match = null;
    }

    @Test
    @DisplayName("Test: creating a match with success")
    void test_CreatingAMatchWithSuccess() {
        assertEquals(MatchStatus.SCHEDULED, match.getStatus());
        assertTrue(match.getHomeGoals().isEmpty());
        assertTrue(match.getAwayGoals().isEmpty());
    }

    @Test
    @DisplayName("Test: creating a match with the same home and away team should throw")
    void test_CreatingMatchWithSameHomeAndAwayTeam_ShouldThrow() {
        assertThrows(InvalidMatchException.class, () ->
            new Match(
                UUID.randomUUID(), 215662L, leagueId, seasonId, homeTeamId, homeTeamId,
                null, matchDate, MatchStatus.SCHEDULED, null, null,
                "Regular Season - 10", null
            ), () -> "Home team and away cannot be the same"
        );
    }

    @Test
    @DisplayName("Test: creating a match with invalid external id should throw")
    void test_CreatingMatchWithInvalidExternalId_ShouldThrow() {
        assertThrows(InvalidExternalIdException.class, () ->
            new Match(
                UUID.randomUUID(), 0L, leagueId, seasonId, homeTeamId, awayTeamId,
                null, matchDate, MatchStatus.SCHEDULED, null, null,
                "Regular Season - 10", null
            )
        );
    }

    @Test
    @DisplayName("Test: a finished match with more home goals should have the home team as winner")
    void test_FinishedMatchWithMoreHomeGoals_ShouldHaveHomeTeamAsWinner() {
        match.setHomeGoals(3);
        match.setAwayGoals(0);
        match.setStatus(MatchStatus.FINISHED);

        assertTrue(match.getWinnerId().isPresent());
        assertEquals(match.getWinnerId().get(), match.getHomeTeamId());
    }

    @Test
    @DisplayName("Test: a finished match with equal goals should have no winner")
    void test_FinishedMatchWithEqualGoals_ShouldHaveNoWinner() {
        match.setHomeGoals(0);
        match.setAwayGoals(0);

        assertTrue(match.getWinnerId().isEmpty());
    }

    @Test
    @DisplayName("Test: a scheduled match should have no winner regardless of goals")
    void test_ScheduledMatch_ShouldHaveNoWinner() {
        assertTrue(match.getWinnerId().isEmpty());
    }
}