package io.github.williamandradesantana.sports.domain.competition;

import io.github.williamandradesantana.sports.domain.competition.exceptions.InvalidStandingException;
import io.github.williamandradesantana.sports.domain.competition.exceptions.InvalidStandingRecordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StandingTest {

    private final StandingRecord sampleRecord = new StandingRecord(
        38, 23, 10, 5, 59, 29
    );

    @Test
    @DisplayName("Test: creating a standing with success")
    void test_CreatingAStandingWithSuccess() {
        Standing standing = new Standing(
            UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 1, 79, "Serie A",
            "WWWDD", StandingTrend.SAME, "CONMEBOL Libertadores", sampleRecord,
            new StandingRecord(19, 12, 5, 2, 31, 13),
            new StandingRecord(19, 11, 5, 3, 28, 16),
            OffsetDateTime.parse("2024-12-11T00:00:00Z")
        );

        assertEquals(1, standing.getRank());
        assertEquals(30, standing.getOverall().goalDifference());
    }

    @Test
    @DisplayName("Test: creating a standing without a season should throw")
    void test_CreatingStandingWithoutSeason_ShouldThrow() {
        assertThrows(InvalidStandingException.class, () -> {
            new Standing(
                UUID.randomUUID(), null, UUID.randomUUID(), 1, 79, "Serie A",
                "WWWDD", StandingTrend.SAME, "CONMEBOL Libertadores", sampleRecord,
                new StandingRecord(19, 12, 5, 2, 31, 13),
                new StandingRecord(19, 11, 5, 3, 28, 16),
                OffsetDateTime.parse("2024-12-11T00:00:00Z")
            );
        });
    }

    @Test
    @DisplayName("Test: creating a standing with non-positive rank should throw")
    void test_CreatingStandingWithNonPositiveRank_ShouldThrow() {
        assertThrows(InvalidStandingException.class, () ->
                new Standing(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 0, 79, "Serie A", null,
                        StandingTrend.SAME, null, sampleRecord, sampleRecord, sampleRecord, OffsetDateTime.now()));
    }

    @Test
    @DisplayName("Test: creating a StandingRecord with negative values should throw")
    void test_CreatingStandingRecordWithNegativeValues_ShouldThrow() {
        assertThrows(InvalidStandingRecordException.class, () ->
                new StandingRecord(-1, 0, 0, 0, 0, 0)
        );
    }

    @Test
    @DisplayName("Test: updating from external source should change rank and points, keep identity")
    void test_UpdatingFromExternalSource_ShouldKeepIdentity() {
        Standing standing = new Standing(
                UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), 3, 70, "Serie A", "WWDLW",
                StandingTrend.UP, null, sampleRecord, sampleRecord, sampleRecord, OffsetDateTime.now()
        );
        UUID originalId = standing.getId();

        standing.updateFromExternalSource(1, 79, "Serie A", "WWWDD", StandingTrend.SAME,
                "CONMEBOL Libertadores", sampleRecord, sampleRecord, sampleRecord, OffsetDateTime.now());

        assertEquals(originalId, standing.getId());
        assertEquals(1, standing.getRank());
        assertEquals(79, standing.getPoints());
    }
}