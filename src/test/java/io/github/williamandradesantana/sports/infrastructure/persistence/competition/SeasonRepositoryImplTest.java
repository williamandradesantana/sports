package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueType;
import io.github.williamandradesantana.sports.infrastructure.persistence.PostgresIntegrationTest;
import io.github.williamandradesantana.sports.infrastructure.persistence.league.LeaguePersistenceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({CompetitionPersistenceConfig.class, LeaguePersistenceConfig.class})
class SeasonRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    private Season season;
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
        season = new Season(
            UUID.randomUUID(), league.getId(), 2024,
            LocalDate.of(2024, 8, 17),
            LocalDate.of(2025, 5, 25), true
        );
    }

    @Test
    @DisplayName("Test: saving a season and finding by league and year should return the same data")
    void test_SavingASeason_ShouldBeFoundByLeagueAndYear() {
        leagueRepository.save(league);
        seasonRepository.save(season);

        Optional<Season> found = seasonRepository.findByLeagueIdAndYear(season.getLeagueId(), season.getYear());

        assertTrue(found.isPresent());
        assertEquals(2024, found.get().getStartDate().getYear());
        assertTrue(found.get().isCurrent());
    }

    @Test
    @DisplayName("Test: finding the current season for a league should return it")
    void test_FindingCurrentSeason_ShouldReturnIt() {
        Season newSeason = new Season(
            UUID.randomUUID(), season.getLeagueId(), 2023,
            LocalDate.of(2023, 8, 12),
            LocalDate.of(2024, 5, 19),
            false
        );
        leagueRepository.save(league);
        seasonRepository.save(newSeason);
        seasonRepository.save(season);

        Optional<Season> current = seasonRepository.findCurrentByLeagueId(season.getLeagueId());

        assertTrue(current.get().isCurrent());
        assertEquals(2024, current.get().getStartDate().getYear());
    }
}