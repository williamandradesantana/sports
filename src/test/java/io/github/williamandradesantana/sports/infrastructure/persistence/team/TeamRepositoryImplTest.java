package io.github.williamandradesantana.sports.infrastructure.persistence.team;

import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.Venue;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;
import io.github.williamandradesantana.sports.infrastructure.persistence.PostgresIntegrationTest;
import io.github.williamandradesantana.sports.infrastructure.persistence.league.LeaguePersistenceConfig;
import io.github.williamandradesantana.sports.infrastructure.persistence.venue.VenuePersistenceConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ TeamPersistenceConfig.class, VenuePersistenceConfig.class})
class TeamRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private VenueRepository venueRepository;

    private Team team;
    private Venue venue;

    @BeforeEach
    void setup() {
        UUID venueId = UUID.randomUUID();
        team = new Team(
            UUID.randomUUID(), 33L, "Manchester United", "MUN", "England",
            1878, false, "https://media.api-sports.io/football/teams/33.png", venueId
        );
        venue = new Venue(
            UUID.randomUUID(), 556L, "Old Trafford", "Sir Matt Busby Way",
            "Manchester", 76212, "grass", "https://media.api-sports.io/football/venues/556.png"
        );
    }

    @AfterEach
    void afterEach() {
        team = null;
        venue = null;
    }

    @Test
    @DisplayName("Test: saving a national team without a venue should round-trip correctly")
    void test_SavingNationalTeamWithoutVenue_ShouldRoundTrip() {
        team.setVenueId(null);

        teamRepository.save(team);

        Optional<Team> found = teamRepository.findByExternalId(33L);
        assertTrue(found.isPresent());
        assertTrue(found.get().getVenueId().isEmpty());
    }

    @Test
    @DisplayName("Test: saving a club team with a venue reference should round-trip correctly")
    void test_SavingClubTeamWithVenue_ShouldRoundTrip() {
        venueRepository.save(venue);
        team.setVenueId(venue.getId());
        teamRepository.save(team);

        Optional<Team> found = teamRepository.findByExternalId(33L);

        assertTrue(found.isPresent());
        assertEquals(venue.getId(), found.get().getVenueId().orElseThrow());
    }
}