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
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    MatchPersistenceConfig.class,
    CompetitionPersistenceConfig.class,
    LeaguePersistenceConfig.class,
    TeamPersistenceConfig.class,
    VenuePersistenceConfig.class
})
class MatchStatisticsRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private MatchStatisticsRepository statisticsRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SeasonRepository seasonRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    private Match match;
    private Team homeTeam;
    private Team awayTeam;

    @BeforeEach
    void setUp() {
        League league = new League(UUID.randomUUID(), 71L, "Serie A", LeagueType.LEAGUE, "logo.png",
                new Country("Brazil", "BR", "flag.svg"));
        leagueRepository.save(league);

        Season season = new Season(UUID.randomUUID(), league.getId(), 2024,
                LocalDate.of(2024, 4, 1), LocalDate.of(2024, 12, 1), true);
        seasonRepository.save(season);

        homeTeam = new Team(UUID.randomUUID(), 131L, "Corinthians", "COR", "Brazil",
                1910, false, "logo.png", null);
        teamRepository.save(homeTeam);

        awayTeam = new Team(UUID.randomUUID(), 124L, "Fluminense", "FLU", "Brazil",
                1902, false, "logo.png", null);
        teamRepository.save(awayTeam);

        match = new Match(UUID.randomUUID(), 1180390L, league.getId(), season.getId(),
                homeTeam.getId(), awayTeam.getId(), null,
                OffsetDateTime.parse("2024-04-28T19:00:00Z"), MatchStatus.FINISHED,
                1, 0, "Round 4", "Ramon Abatti");
        matchRepository.save(match);
    }

    @Test
    @DisplayName("Test: saving statistics for both teams should be found by matchId")
    void test_SavingStatisticsForBothTeams_ShouldBeFoundByMatchId() {
        MatchStatistics homeStats = new MatchStatistics(
            UUID.randomUUID(), match.getId(), homeTeam.getId(),
            3, 2, 9, 4, 4,
            5, 22, 3, 1, 32, 5, 1,
            0, 242, 121, 50
        );

        MatchStatistics awayStats = new MatchStatistics(
            UUID.randomUUID(), match.getId(), awayTeam.getId(),
            0, 3, 7, 4, 4, 3, 10,
            5, 9, 68, 5, 0, 2,
            514, 397, 77
        );

        statisticsRepository.save(homeStats);
        statisticsRepository.save(awayStats);

        List<MatchStatistics> found = statisticsRepository.findByMatchId(match.getId());
        assertEquals(2, found.size());
    }

    @Test
    @DisplayName("Test: saving statistics with null metrics should round-trip correctly")
    void test_SavingStatisticsWithNullMetrics_ShouldRoundTrip() {
        MatchStatistics stats = new MatchStatistics(
            UUID.randomUUID(), match.getId(), homeTeam.getId(),
            null, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null
        );

        statisticsRepository.save(stats);

        Optional<MatchStatistics> found = statisticsRepository.findByMatchIdAndTeamId(match.getId(), homeTeam.getId());

        assertTrue(found.isPresent());
        assertTrue(found.get().getShotsOnGoal().isEmpty());
    }
}