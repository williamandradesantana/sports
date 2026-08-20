package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import io.github.williamandradesantana.sports.domain.competition.*;
import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueType;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.infrastructure.persistence.PostgresIntegrationTest;
import io.github.williamandradesantana.sports.infrastructure.persistence.league.LeaguePersistenceConfig;
import io.github.williamandradesantana.sports.infrastructure.persistence.team.TeamPersistenceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({CompetitionPersistenceConfig.class, LeaguePersistenceConfig.class, TeamPersistenceConfig.class})
class StandingRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private StandingRepository standingRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Season season;
    private Team team;

    @BeforeEach
    void setup() {
        League league = new League(UUID.randomUUID(), 71L, "Serie A", LeagueType.LEAGUE, "logo.png",
                new Country("Brazil", "BR", "flag.svg"));
        leagueRepository.save(league);

        season = new Season(UUID.randomUUID(), league.getId(), 2024,
                LocalDate.of(2024, 4, 1), LocalDate.of(2024, 12, 1), true);
        seasonRepository.save(season);

        team = new Team(UUID.randomUUID(), 120L, "Botafogo", "BOT", "Brazil",
                1904, false, "logo.png", null);
        teamRepository.save(team);
    }

    @Test
    @DisplayName("Test: saving a standing and finding by season and team should round-trip correctly")
    void test_SavingStanding_ShouldRoundTrip() {
        StandingRecord overall = new StandingRecord(38, 23, 10, 5, 59, 29);
        StandingRecord home = new StandingRecord(19, 12, 5, 2, 31, 13);
        StandingRecord away = new StandingRecord(19, 11, 5, 3, 28, 16);

        Standing standing = new Standing(
                UUID.randomUUID(), season.getId(), team.getId(), 1, 79, "Serie A", "WWWDD",
                StandingTrend.SAME, "CONMEBOL Libertadores", overall, home, away,
                OffsetDateTime.parse("2024-12-11T00:00:00Z")
        );

        standingRepository.save(standing);

        List<Standing> found = standingRepository.findBySeasonId(season.getId());

        assertEquals(1, found.size());
        assertEquals(30, found.get(0).getOverall().goalDifference());
        assertEquals(StandingTrend.SAME, found.get(0).getTrend());
    }
}