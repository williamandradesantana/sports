package io.github.williamandradesantana.sports.infrastructure.persistence.integrity;

import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessment;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessmentRepository;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityFactor;
import io.github.williamandradesantana.sports.domain.integrity.RiskLevel;
import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueType;
import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.match.MatchStatus;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.infrastructure.persistence.PostgresIntegrationTest;
import io.github.williamandradesantana.sports.infrastructure.persistence.competition.CompetitionPersistenceConfig;
import io.github.williamandradesantana.sports.infrastructure.persistence.league.LeaguePersistenceConfig;
import io.github.williamandradesantana.sports.infrastructure.persistence.match.MatchPersistenceConfig;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({
    IntegrityPersistenceConfig.class, MatchPersistenceConfig.class, LeaguePersistenceConfig.class,
    CompetitionPersistenceConfig.class, TeamPersistenceConfig.class, VenuePersistenceConfig.class
})
class IntegrityAssessmentRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private IntegrityAssessmentRepository integrityAssessmentRepository;

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
        League league = new League(UUID.randomUUID(), 71L, "Serie A", LeagueType.LEAGUE, "logo.png",
                new Country("Brazil", "BR", "flag.svg"));
        leagueRepository.save(league);

        Season season = new Season(UUID.randomUUID(), league.getId(), 2024,
                LocalDate.of(2024, 4, 1), LocalDate.of(2024, 12, 1), true);
        seasonRepository.save(season);

        Team home = new Team(UUID.randomUUID(), 131L, "Corinthians", "COR", "Brazil", 1910, false, "logo.png", null);
        teamRepository.save(home);
        Team away = new Team(UUID.randomUUID(), 124L, "Fluminense", "FLU", "Brazil", 1902, false, "logo.png", null);
        teamRepository.save(away);

        match = new Match(UUID.randomUUID(), 1180390L, league.getId(), season.getId(), home.getId(), away.getId(),
                null, OffsetDateTime.now(), MatchStatus.FINISHED, 1, 0, null, null);
        matchRepository.save(match);
    }

    @Test
    @DisplayName("Test: saving an assessment should persist it together with its factors")
    void test_SavingAssessment_ShouldPersistWithFactors() {
        List<IntegrityFactor> factors = List.of(
                new IntegrityFactor("DOMINANT_TEAM_LOST", "The dominant team lost the match", 35),
                new IntegrityFactor("INEFFICIENT_VICTORY", "Large margin win with few shots on target", 25)
        );
        IntegrityAssessment assessment = new IntegrityAssessment(UUID.randomUUID(), match.getId(), 60, factors,
                OffsetDateTime.now());

        integrityAssessmentRepository.save(assessment);

        Optional<IntegrityAssessment> found = integrityAssessmentRepository.findLatestByMatchId(match.getId());

        assertTrue(found.isPresent());
        assertEquals(60, found.get().getScore());
        assertEquals(RiskLevel.HIGH, found.get().getRiskLevel());
        assertEquals(2, found.get().getFactors().size());
        assertTrue(found.get().getFactors().stream().anyMatch(f -> f.code().equals("DOMINANT_TEAM_LOST")));
    }

    @Test
    @DisplayName("Test: saving an assessment with no factors should persist an empty list")
    void test_SavingAssessmentWithNoFactors_ShouldPersistEmptyList() {
        IntegrityAssessment assessment = new IntegrityAssessment(UUID.randomUUID(), match.getId(), 0, List.of(),
                OffsetDateTime.now());

        integrityAssessmentRepository.save(assessment);

        Optional<IntegrityAssessment> found = integrityAssessmentRepository.findLatestByMatchId(match.getId());

        assertTrue(found.isPresent());
        assertTrue(found.get().getFactors().isEmpty());
    }

    @Test
    @DisplayName("Test: finding latest should return the most recently assessed snapshot")
    void test_FindingLatest_ShouldReturnMostRecent() {
        integrityAssessmentRepository.save(new IntegrityAssessment(UUID.randomUUID(), match.getId(), 20, List.of(),
                OffsetDateTime.now().minusHours(2)));
        integrityAssessmentRepository.save(new IntegrityAssessment(UUID.randomUUID(), match.getId(), 55, List.of(),
                OffsetDateTime.now()));

        Optional<IntegrityAssessment> found = integrityAssessmentRepository.findLatestByMatchId(match.getId());

        assertEquals(55, found.get().getScore());
    }
}