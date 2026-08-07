package io.github.williamandradesantana.sports.domain.league;

import io.github.williamandradesantana.sports.domain.league.exceptions.InvalidLeagueNameException;
import io.github.williamandradesantana.sports.domain.shared.exceptions.InvalidExternalIdException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LeagueTest {

    private League league;
    private Country country;
    private LeagueType type;

    @BeforeEach
    void setup() {
        // Given - Arrange
        country = new Country("England", "GB-ENG", "https://media.api-sports.io/flags/gb-eng.svg");
        type = LeagueType.LEAGUE;
        league = new League(
            UUID.randomUUID(),
            39L,
            "Premier League",
            type,
            "https://media.api-sports.io/football/leagues/39.png",
            country
        );
    }

    @AfterEach
    void afterEach() {
        country = null;
        type = null;
        league = null;
    }

    @Test
    @DisplayName("Test: creating a league with success")
    void test_WhenALeagueContainsAllFieldsCorrects_ShouldCreateALeague() {
        assertNotNull(league, () -> "The league cannot be null");
        assertEquals(39L, league.getExternalId(), () -> "The externalId not matches!");
        assertEquals("Premier League", league.getName(), () -> "The league name not matches!");
        assertEquals("LEAGUE", league.getType().name(), () -> "The league type not matches!");
        assertEquals("https://media.api-sports.io/football/leagues/39.png", league.getLogoUrl(),
                () -> "The league logo url not matches!");
        assertEquals("England", league.getCountry().name(), () -> "Country name not matches!");
        assertEquals("GB-ENG", league.getCountry().code(), () -> "Country code not matches!");
        assertEquals("https://media.api-sports.io/flags/gb-eng.svg",
                league.getCountry().flagUrl(), () -> "Country flag url not matches!");
    }

    @Test
    @DisplayName("Test: creating a league with external id non-positive or null should return exception")
    void test_CreatingLeagueWithInvalidExternalId_ShouldThrow() {
        assertThrows(InvalidExternalIdException.class, () -> league = new League(
                UUID.randomUUID(),
                -1L,
                "Premier League",
                type,
                "https://media.api-sports.io/football/leagues/39.png",
                country
        ), () -> "The external id cannot be negative");

        assertThrows(InvalidExternalIdException.class, () -> league = new League(
                UUID.randomUUID(),
                null,
                "Premier League",
                type,
                "https://media.api-sports.io/football/leagues/39.png",
                country
        ), () -> "The external id cannot be null");
    }

    @Test
    @DisplayName("Test: creating a league with blank name should throw InvalidLeagueNameException")
    void test_CreatingLeagueWithBlankName_ShouldThrow() {
        InvalidLeagueNameException exception = assertThrows(
            InvalidLeagueNameException.class,
            () -> league.setName(""),
            () -> "The league name cannot be blank!"
        );

        assertEquals("League name cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Test: updating from external source should change mutable fields but keep identity")
    void test_UpdatingFromExternalSource_ShouldKeepIdentity() {
        UUID originalId = league.getId();
        Long originalExternalId = league.getExternalId();

        league.updateFromExternalSource("Premier League Updated", LeagueType.LEAGUE,
                "https://new-logo.png", country);

        assertEquals(originalId, league.getId());
        assertEquals(originalExternalId, league.getExternalId());
        assertEquals("Premier League Updated", league.getName());
        assertEquals("https://new-logo.png", league.getLogoUrl());
    }
}