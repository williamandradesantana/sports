package io.github.williamandradesantana.sports.domain.integrity;

import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchStatistics;
import io.github.williamandradesantana.sports.domain.match.MatchStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IntegrityScoringServiceTest {

    private final IntegrityScoringService service = new IntegrityScoringService();

    @Test
    @DisplayName("Test: a normal match with proportional stats and result should score low risk")
    void test_NormalMatch_ShouldScoreLowRisk() {
        UUID homeTeamId = UUID.randomUUID();
        UUID awayTeamId = UUID.randomUUID();
        Match match = new Match(UUID.randomUUID(), 1L, UUID.randomUUID(), UUID.randomUUID(),
                homeTeamId, awayTeamId, null, OffsetDateTime.now(), MatchStatus.FINISHED, 2, 1, null, null);

        MatchStatistics home = new MatchStatistics(UUID.randomUUID(), match.getId(), homeTeamId,
                5, 4, 12, 3, 8, 4, 10, 5, 2, 55, 2, 0, 3, 400, 320, 80);
        MatchStatistics away = new MatchStatistics(UUID.randomUUID(), match.getId(), awayTeamId,
                3, 5, 9, 1, 6, 3, 12, 4, 1, 45, 3, 0, 4, 350, 270, 77);

        IntegrityAssessment assessment = service.assess(match, home, away);

        assertEquals(RiskLevel.LOW, assessment.getRiskLevel());
        assertTrue(assessment.getFactors().isEmpty());
    }

    @Test
    @DisplayName("Test: the dominant team losing should trigger DOMINANT_TEAM_LOST")
    void test_DominantTeamLosing_ShouldTriggerFactor() {
        UUID homeTeamId = UUID.randomUUID();
        UUID awayTeamId = UUID.randomUUID();
        Match match = new Match(UUID.randomUUID(), 1L, UUID.randomUUID(), UUID.randomUUID(),
                homeTeamId, awayTeamId, null, OffsetDateTime.now(), MatchStatus.FINISHED, 0, 1, null, null);

        MatchStatistics home = new MatchStatistics(UUID.randomUUID(), match.getId(), homeTeamId,
                10, 8, 22, 4, 15, 7, 8, 9, 1, 68, 1, 0, 2, 500, 400, 80);
        MatchStatistics away = new MatchStatistics(UUID.randomUUID(), match.getId(), awayTeamId,
                2, 1, 4, 1, 3, 1, 10, 2, 0, 32, 2, 0, 5, 200, 150, 75);

        IntegrityAssessment assessment = service.assess(match, home, away);

        assertTrue(assessment.getFactors().stream().anyMatch(f -> f.code().equals("DOMINANT_TEAM_LOST")));
        assertTrue(assessment.getScore() >= 35);
    }

    @Test
    @DisplayName("Test: high possession with zero goals while conceding two should trigger POSSESSION_WITHOUT_CONVERSION")
    void test_PossessionWithoutConversion_ShouldTriggerFactor() {
        UUID homeTeamId = UUID.randomUUID();
        UUID awayTeamId = UUID.randomUUID();
        Match match = new Match(UUID.randomUUID(), 1L, UUID.randomUUID(), UUID.randomUUID(),
                homeTeamId, awayTeamId, null, OffsetDateTime.now(), MatchStatus.FINISHED, 0, 2, null, null);

        MatchStatistics home = new MatchStatistics(UUID.randomUUID(), match.getId(), homeTeamId,
                6, 5, 14, 3, 9, 5, 10, 6, 2, 70, 2, 0, 3, 450, 350, 78);
        MatchStatistics away = new MatchStatistics(UUID.randomUUID(), match.getId(), awayTeamId,
                4, 2, 7, 1, 5, 2, 8, 3, 1, 30, 1, 0, 4, 220, 170, 77);

        IntegrityAssessment assessment = service.assess(match, home, away);

        assertTrue(assessment.getFactors().stream().anyMatch(f -> f.code().equals("POSSESSION_WITHOUT_CONVERSION")));
    }

    @Test
    @DisplayName("Test: a match with multiple suspicious factors should reach HIGH or CRITICAL risk")
    void test_MultipleFactors_ShouldReachHighRisk() {
        UUID homeTeamId = UUID.randomUUID();
        UUID awayTeamId = UUID.randomUUID();
        Match match = new Match(UUID.randomUUID(), 1L, UUID.randomUUID(), UUID.randomUUID(),
                homeTeamId, awayTeamId, null, OffsetDateTime.now(), MatchStatus.FINISHED, 0, 3, null, null);

        MatchStatistics home = new MatchStatistics(UUID.randomUUID(), match.getId(), homeTeamId,
                12, 9, 25, 4, 18, 7, 6, 10, 1, 72, 1, 0, 1, 550, 460, 84);
        MatchStatistics away = new MatchStatistics(UUID.randomUUID(), match.getId(), awayTeamId,
                2, 1, 4, 1, 3, 1, 9, 3, 0, 28, 1, 0, 8, 180, 130, 72);

        IntegrityAssessment assessment = service.assess(match, home, away);

        assertTrue(assessment.getFactors().size() >= 2);
        assertTrue(assessment.getRiskLevel() == RiskLevel.HIGH || assessment.getRiskLevel() == RiskLevel.CRITICAL);
    }

    @Test
    @DisplayName("Test: score should never exceed 100 even when all factors trigger")
    void test_ScoreShouldNeverExceed100() {
        UUID homeTeamId = UUID.randomUUID();
        UUID awayTeamId = UUID.randomUUID();
        Match match = new Match(UUID.randomUUID(), 1L, UUID.randomUUID(), UUID.randomUUID(),
                homeTeamId, awayTeamId, null, OffsetDateTime.now(), MatchStatus.FINISHED, 0, 3, null, null);

        MatchStatistics home = new MatchStatistics(UUID.randomUUID(), match.getId(), homeTeamId,
                15, 10, 30, 5, 20, 8, 5, 12, 0, 80, 0, 0, 0, 600, 550, 90);
        MatchStatistics away = new MatchStatistics(UUID.randomUUID(), match.getId(), awayTeamId,
                1, 0, 2, 0, 1, 1, 10, 1, 0, 20, 1, 0, 10, 100, 60, 60);

        IntegrityAssessment assessment = service.assess(match, home, away);

        assertTrue(assessment.getScore() <= 100);
    }
}