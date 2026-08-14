package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueType;
import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.match.MatchStatus;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.Venue;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;
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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    MatchPersistenceConfig.class,
    LeaguePersistenceConfig.class,
    CompetitionPersistenceConfig.class,
    TeamPersistenceConfig.class,
    VenuePersistenceConfig.class
})
class MatchRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private LeagueRepository leagueRepository;
    @Autowired
    private SeasonRepository seasonRepository;

    private League league;
    private Season season;
    private Team homeTeam;
    private Team awayTeam;

    @BeforeEach
    void setup() {
        // given - arrange
        Country country = new Country("England", "GB-ENG", "https://media.api-sports.io/flags/gb-eng.svg");

        Venue venueHomeTeam = new Venue(
                UUID.randomUUID(), 556L, "Old Trafford", "Sir Matt Busby Way",
                "Manchester", 76212, "grass", "https://media.api-sports.io/football/venues/556.png"
        );
        venueRepository.save(venueHomeTeam);

        Venue venueAwayTeam = new Venue(
                UUID.randomUUID(), 557L, "Anfield", "Liverpool",
                "Liverpool", 54074, "grass", "https://media.api-sports.io/football/venues/557.png"
        );
        venueRepository.save(venueAwayTeam);

        league = new League(
            UUID.randomUUID(), 39L, "Premier League", LeagueType.LEAGUE,
            "https://media.api-sports.io/football/leagues/39.png", country
        );
        leagueRepository.save(league);

        season = new Season(
            UUID.randomUUID(), league.getId(), 2024,
            LocalDate.of(2024, 8, 17), LocalDate.of(2025, 5, 25), false
        );
        seasonRepository.save(season);

        homeTeam = new Team(
            UUID.randomUUID(), 33L, "Manchester United", "MUN", "England",
            1878, false, "https://media.api-sports.io/football/teams/33.png", venueHomeTeam.getId()
        );
        teamRepository.save(homeTeam);

        awayTeam = new Team(
            UUID.randomUUID(), 34L, "Liverpool", "LIV", "England",
            1892, false, "https://media.api-sports.io/football/teams/34.png", venueAwayTeam.getId()
        );
        teamRepository.save(awayTeam);
    }

    @Test
    @DisplayName("Test: saving a scheduled match and finding by external id should return the same data")
    void test_SavingAScheduledMatch_ShouldBeFoundByExternalId() {
        Match match = new Match(
            UUID.randomUUID(), 215662L, league.getId(), season.getId(),
            homeTeam.getId(), awayTeam.getId(), null,
            OffsetDateTime.parse("2026-08-21T14:00:00Z"), MatchStatus.SCHEDULED,
            null, null, "Round 1", null
        );

        matchRepository.save(match);

        Optional<Match> found = matchRepository.findByExternalId(215662L);

        assertTrue(found.isPresent());
        assertEquals(homeTeam.getId(), found.get().getHomeTeamId());
        assertEquals(MatchStatus.SCHEDULED, found.get().getStatus());
    }
}