package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueType;
import io.github.williamandradesantana.sports.domain.match.*;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.infrastructure.persistence.PostgresIntegrationTest;
import io.github.williamandradesantana.sports.infrastructure.persistence.competition.CompetitionPersistenceConfig;
import io.github.williamandradesantana.sports.infrastructure.persistence.league.LeaguePersistenceConfig;
import io.github.williamandradesantana.sports.infrastructure.persistence.team.TeamPersistenceConfig;
import io.github.williamandradesantana.sports.infrastructure.persistence.venue.VenuePersistenceConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    MatchPersistenceConfig.class, LeaguePersistenceConfig.class,
    CompetitionPersistenceConfig.class, TeamPersistenceConfig.class, VenuePersistenceConfig.class
})
class OddsRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private OddsRepository oddsRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Match match;

    @BeforeEach
    void setup() {
        League league = new League(UUID.randomUUID(), 253L, "MLS", LeagueType.LEAGUE, "logo.png",
                new Country("USA", "US", "flag.svg"));
        leagueRepository.save(league);

        Season season = new Season(UUID.randomUUID(), league.getId(), 2026,
                LocalDate.of(2026, 2, 1), LocalDate.of(2026, 12, 1), true);
        seasonRepository.save(season);

        Team home = new Team(UUID.randomUUID(), 1L, "Team A", "TMA", "USA", null, false, "logo.png", null);
        teamRepository.save(home);
        Team away = new Team(UUID.randomUUID(), 2L, "Team B", "TMB", "USA", null, false, "logo.png", null);
        teamRepository.save(away);

        match = new Match(UUID.randomUUID(), 1490391L, league.getId(), season.getId(),
                home.getId(), away.getId(), null, OffsetDateTime.parse("2026-08-19T23:30:00Z"),
                MatchStatus.SCHEDULED, null, null, null, null);
        matchRepository.save(match);
    }

    @Test
    @DisplayName("Test: saving multiple odds snapshots for the same match and bookmaker should not conflict")
    void test_SavingMultipleSnapshots_ShouldNotConflict() {
        Odds firstSnapshot = new Odds(UUID.randomUUID(), match.getId(), 8L, "Bet365",
                OffsetDateTime.parse("2026-08-17T10:00:00Z"),
                new BigDecimal("2.40"), new BigDecimal("3.90"), new BigDecimal("2.62"),
                new BigDecimal("1.40"), new BigDecimal("2.88"), new BigDecimal("1.40"), new BigDecimal("2.75"));

        Odds secondSnapshot = new Odds(UUID.randomUUID(), match.getId(), 8L, "Bet365",
                OffsetDateTime.parse("2026-08-19T22:00:00Z"),
                new BigDecimal("2.10"), new BigDecimal("3.70"), new BigDecimal("3.10"),
                new BigDecimal("1.35"), new BigDecimal("3.00"), new BigDecimal("1.45"), new BigDecimal("2.60"));

        oddsRepository.save(firstSnapshot);
        oddsRepository.save(secondSnapshot);

        List<Odds> history = oddsRepository.findByMatchId(match.getId());

        assertEquals(2, history.size(), () -> "Expected both snapshots to persist without conflict");
        assertEquals(new BigDecimal("2.40"), history.get(0).getHomeWinOdd().orElseThrow());
        assertEquals(new BigDecimal("2.10"), history.get(1).getHomeWinOdd().orElseThrow());
    }

    @Test
    @DisplayName("Test: finding the latest odds should return the most recently captured snapshot")
    void test_FindingLatestOdds_ShouldReturnMostRecent() {
        oddsRepository.save(new Odds(UUID.randomUUID(), match.getId(), 8L, "Bet365",
                OffsetDateTime.parse("2026-08-17T10:00:00Z"),
                new BigDecimal("2.40"), null, null, null, null, null, null));
        oddsRepository.save(new Odds(UUID.randomUUID(), match.getId(), 8L, "Bet365",
                OffsetDateTime.parse("2026-08-19T22:00:00Z"),
                new BigDecimal("2.10"), null, null, null, null, null, null));

        Optional<Odds> latest = oddsRepository.findLatestByMatchId(match.getId());

        assertTrue(latest.isPresent());
        assertEquals(new BigDecimal("2.10"), latest.get().getHomeWinOdd().orElseThrow());
    }
}