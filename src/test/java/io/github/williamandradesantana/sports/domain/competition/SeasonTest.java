package io.github.williamandradesantana.sports.domain.competition;

import io.github.williamandradesantana.sports.domain.competition.exceptions.InvalidSeasonException;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.shared.exceptions.InvalidExternalIdException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SeasonTest {

    private Season season;

    @BeforeEach
    void setUp() {
        season = new Season(
            UUID.randomUUID(),
            UUID.randomUUID(),
            2024,
            LocalDate.of(2024, 8, 17),
            LocalDate.of(2025, 5, 25),
            false
        );
    }

    @AfterEach
    void afterEach() {
        season = null;
    }

    @Test
    @DisplayName("Test: creating a season with success")
    void test_WhenASeasonContainsAllFieldsCorrects_ShouldCreateASeason() {
        assertNotNull(season);
        assertEquals(2024, season.getYear());
        assertEquals(LocalDate.of(2024, 8, 17), season.getStartDate());
        assertEquals(LocalDate.of(2025, 5, 25), season.getEndDate());
        assertEquals(LocalDate.of(2025, 5, 25), season.getEndDate());
        assertFalse(season.isCurrent());
    }

    @Test
    @DisplayName("Test: creating a season without a league id should throw InvalidSeasonException")
    void test_CreatingSeasonWithoutLeagueId_ShouldThrow() {
        assertThrows(InvalidSeasonException.class, () -> new Season(
            UUID.randomUUID(), null, 2024,
            LocalDate.of(2024, 8, 17),
            LocalDate.of(2025, 5, 25), false)
        );
    }

    @Test
    @DisplayName("Test: creating a season with end date before start date should throw")
    void test_CreatingSeasonWithEndBeforeStart_ShouldThrow() {
        assertThrows(InvalidSeasonException.class, () -> new Season(
                UUID.randomUUID(), null, 2024,
                LocalDate.of(2025, 5, 25),
                LocalDate.of(2024, 8, 17),
                 false)
        );
    }

    @Test
    @DisplayName("Test: marking a season as current should update the flag")
    void test_MarkingSeasonAsCurrent_ShouldUpdateFlag() {
        season.markAsCurrent();
        assertTrue(season.isCurrent());
    }

}