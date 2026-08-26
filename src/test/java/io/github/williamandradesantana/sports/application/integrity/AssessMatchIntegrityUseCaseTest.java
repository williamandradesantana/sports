package io.github.williamandradesantana.sports.application.integrity;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessment;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityAssessmentRepository;
import io.github.williamandradesantana.sports.domain.integrity.IntegrityScoringService;
import io.github.williamandradesantana.sports.domain.match.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessMatchIntegrityUseCaseTest {


    @Mock
    private MatchRepository matchRepository;

    @Mock
    private MatchStatisticsRepository matchStatisticsRepository;

    @Mock
    private IntegrityAssessmentRepository integrityAssessmentRepository;

    private final IntegrityScoringService realScoringService = new IntegrityScoringService();
    private AssessMatchIntegrityUseCase useCase;

    private UUID matchId;
    private UUID homeTeamId;
    private UUID awayTeamId;

    @BeforeEach
    void setup() {
        useCase = new AssessMatchIntegrityUseCase(matchRepository, integrityAssessmentRepository,
                realScoringService, matchStatisticsRepository);

        matchId = UUID.randomUUID();
        homeTeamId = UUID.randomUUID();
        awayTeamId = UUID.randomUUID();
    }

    @Test
    @DisplayName("Test: assessing a finished match with both teams' statistics should succeed and save")
    void test_AssessingFinishedMatchWithStatistics_ShouldSaveAssessment() {
        Match match = new Match(matchId, 1L, UUID.randomUUID(), UUID.randomUUID(), homeTeamId, awayTeamId,
                null, OffsetDateTime.now(), MatchStatus.FINISHED, 2, 1, null, null);
        MatchStatistics home = new MatchStatistics(UUID.randomUUID(), matchId, homeTeamId,
                5, 4, 12, 3, 8, 4, 10, 5, 2, 55, 2, 0, 3, 400, 320, 80);
        MatchStatistics away = new MatchStatistics(UUID.randomUUID(), matchId, awayTeamId,
                3, 5, 9, 1, 6, 3, 12, 4, 1, 45, 3, 0, 4, 350, 270, 77);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchStatisticsRepository.findByMatchId(matchId)).thenReturn(List.of(home, away));

        IntegrityAssessment result = useCase.execute(matchId);

        assertEquals(matchId, result.getMatchId());
        verify(integrityAssessmentRepository).save(result);
    }

    @Test
    @DisplayName("Test: assessing a match that is not finished should throw")
    void test_AssessingUnfinishedMatch_ShouldThrow() {
        Match match = new Match(matchId, 1L, UUID.randomUUID(), UUID.randomUUID(), homeTeamId, awayTeamId,
                null, OffsetDateTime.now(), MatchStatus.SCHEDULED, null, null, null, null);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));

        assertThrows(MatchNotAssessableException.class, () -> useCase.execute(matchId));

        verifyNoInteractions(matchStatisticsRepository, integrityAssessmentRepository);
    }

    @Test
    @DisplayName("Test: assessing when statistics are missing should throw")
    void test_AssessingWhenStatisticsMissing_ShouldThrow() {
        Match match = new Match(matchId, 1L, UUID.randomUUID(), UUID.randomUUID(), homeTeamId, awayTeamId,
                null, OffsetDateTime.now(), MatchStatus.FINISHED, 2, 1, null, null);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchStatisticsRepository.findByMatchId(matchId)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(matchId));

        verify(integrityAssessmentRepository, never()).save(any());
    }
}