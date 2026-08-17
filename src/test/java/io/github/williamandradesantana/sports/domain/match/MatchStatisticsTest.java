package io.github.williamandradesantana.sports.domain.match;

import io.github.williamandradesantana.sports.domain.match.exceptions.InvalidMatchStatisticsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MatchStatisticsTest {

    private MatchStatistics statistics;

    @BeforeEach
    void setUp() {
        statistics = new MatchStatistics(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            3, 2, 9, 4, 4, 5, 22,
            3, 1, 32, 5, 1, 0,
            242, 121, 50
        );
    }

    @Test
    @DisplayName("Test: creating match statistics with success")
    void test_CreatingMatchStatisticsWithSuccess() {
        assertNotNull(statistics, () -> "statistics cannot be null");
        assertEquals(3, statistics.getShotsOnGoal().orElseThrow());
        assertEquals(2, statistics.getShotsOffGoal().orElseThrow());
    }

    @Test
    @DisplayName("Test: creating match statistics without a matchId should throw")
    void test_CreatingWithoutMatchId_ShouldThrow() {
        assertThrows(InvalidMatchStatisticsException.class, () -> {
            statistics = new MatchStatistics(
                UUID.randomUUID(), null, UUID.randomUUID(),
                3, 2, 9, 4, 4, 5, 22,
                3, 1, 32, 5, 1, 0,
                242, 121, 50
            );
        });
    }

    @Test
    @DisplayName("Test: creating match statistics with all null metrics should succeed")
    void test_CreatingWithAllNullMetrics_ShouldSucceed() {
        statistics = new MatchStatistics(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(),
            null, null, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null
        );

        assertTrue(statistics.getShotsOnGoal().isEmpty());
        assertTrue(statistics.getBallPossessionPercentage().isEmpty());
    }

    @Test
    @DisplayName("Test: updating from external source should replace all metrics")
    void test_UpdatingFromExternalSource_ShouldReplaceMetrics() {
        statistics.updateFromExternalSource(
            5, 3, 12, 4, 6, 6, 18,
            4, 2, 40, 3, 0, 1,
            300, 200, 66
        );

        assertEquals(5, statistics.getShotsOnGoal().orElseThrow());
        assertEquals(40, statistics.getBallPossessionPercentage().orElseThrow());
    }
}