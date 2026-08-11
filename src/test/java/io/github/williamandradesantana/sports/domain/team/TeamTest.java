package io.github.williamandradesantana.sports.domain.team;

import io.github.williamandradesantana.sports.domain.shared.exceptions.InvalidExternalIdException;
import io.github.williamandradesantana.sports.domain.team.exceptions.InvalidTeamNameException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    private Team team;

    @BeforeEach
    void setup() {
        UUID venueId = UUID.randomUUID();
        team = new Team(
            UUID.randomUUID(), 33L, "Manchester United", "MUN", "England",
            1878, false, "https://media.api-sports.io/football/teams/33.png", venueId
        );
    }

    @AfterEach
    void afterEach() {
        team = null;
    }

    @Test
    @DisplayName("Test: creating a club team with a venue should succeed")
    void test_CreatingClubTeamWithVenue_ShouldSucceed() {
        assertNotNull(team);
        assertEquals("Manchester United", team.getName());
        assertFalse(team.isNational());
        assertTrue(team.getVenueId().isPresent());
        assertEquals(1878, team.getFounded().orElseThrow());
    }

    @Test
    @DisplayName("Test: creating a national team without a venue should succeed")
    void test_CreatingNationalTeamWithoutVenue_ShouldSucceed() {
        team.setVenueId(null);
        team.setFounded(null);
        team.setName("Brazil");
        team.setCode("BRA");
        team.setCountryName("Brazil");
        team.setNational(true);

        assertTrue(team.isNational());
        assertTrue(team.getVenueId().isEmpty());
        assertTrue(team.getFounded().isEmpty());
    }

    @Test
    @DisplayName("Test: creating a team with blank name should throw")
    void test_CreatingTeamWithBlankName_ShouldThrow() {
        assertThrows(InvalidTeamNameException.class, () -> team.setName(""));
    }

    @Test
    @DisplayName("Test: creating a team with invalid external id should throw")
    void test_CreatingTeamWithInvalidExternalId_ShouldThrow() {
        assertThrows(InvalidExternalIdException.class, () ->
            new Team(
                UUID.randomUUID(), -1L, "Manchester United", "MUN",
                "England", 1878, false, null, null
            )
        );
    }
}