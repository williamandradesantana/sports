package io.github.williamandradesantana.sports.infrastructure.persistence.league;

import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueType;
import io.github.williamandradesantana.sports.infrastructure.persistence.PostgresIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@Import(LeaguePersistenceConfig.class)
class LeagueRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private LeagueRepository leagueRepository;

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

    @Test
    @DisplayName("Test: saving a league and finding by external id should return the same data")
    void test_SavingALeague_ShouldBeFoundByExternalId() {
        // when - act
        leagueRepository.save(league);

        Optional<League> found = leagueRepository.findByExternalId(39L);

        assertNotNull(found);
        assertEquals("Premier League", found.get().getName());
        assertEquals("England", found.get().getCountry().name());
    }

    @Test
    @DisplayName("Test: finding a non-existent external id should return empty")
    void test_FindingNonExistentExternalId_ShouldReturnEmpty() {
        assertTrue(leagueRepository.findByExternalId(99999999L).isEmpty());
    }
}