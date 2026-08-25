package io.github.williamandradesantana.sports.domain.match;

import io.github.williamandradesantana.sports.domain.match.exceptions.InvalidOddsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OddsTest {

    @Test
    @DisplayName("Test: creating odds with success")
    void test_CreatingOddsWithSuccess() {
        Odds odds = new Odds(
                UUID.randomUUID(), UUID.randomUUID(), 8L, "Bet365", OffsetDateTime.now(),
                new BigDecimal("2.40"), new BigDecimal("3.90"), new BigDecimal("2.62"),
                new BigDecimal("1.40"), new BigDecimal("2.88"), new BigDecimal("1.40"), new BigDecimal("2.75")
        );

        assertEquals(new BigDecimal("2.40"), odds.getHomeWinOdd().orElseThrow());
        assertEquals("Bet365", odds.getBookmakerName());
    }

    @Test
    @DisplayName("Test: creating odds without a matchId should throw")
    void test_CreatingOddsWithoutMatchId_ShouldThrow() {
        assertThrows(InvalidOddsException.class, () ->
                new Odds(UUID.randomUUID(), null, 8L, "Bet365", OffsetDateTime.now(),
                        null, null, null, null, null, null, null));
    }

    @Test
    @DisplayName("Test: creating odds with a value of 1.00 or less should throw")
    void test_CreatingOddsWithValueOneOrLess_ShouldThrow() {
        assertThrows(InvalidOddsException.class, () ->
                new Odds(
                    UUID.randomUUID(), UUID.randomUUID(), 8L, "Bet365",
                    OffsetDateTime.now(), BigDecimal.ONE, null, null, null,
                    null, null, null)

        );
    }

    @Test
    @DisplayName("Test: creating odds with only some markets available should succeed")
    void test_CreatingOddsWithPartialMarkets_ShouldSucceed() {
        Odds odds = new Odds(
                UUID.randomUUID(), UUID.randomUUID(), 8L, "Bet365", OffsetDateTime.now(),
                new BigDecimal("2.40"), new BigDecimal("3.90"), new BigDecimal("2.62"),
                null, null, null, null
        );

        assertTrue(odds.getOverGoalsOdd().isEmpty());
        assertTrue(odds.getHomeWinOdd().isPresent());
    }
}