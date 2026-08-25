package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.match.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOddsHistoryUseCaseTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private OddsRepository oddsRepository;

    private GetOddsHistoryUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new GetOddsHistoryUseCase(matchRepository, oddsRepository);
    }

    @Test
    @DisplayName("Test: getting odds history should return all snapshots for the match")
    void test_GettingOddsHistory_ShouldReturnAllSnapshots() {
        UUID matchId = UUID.randomUUID();
        Match match = new Match(matchId, 1L, UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), null, OffsetDateTime.now(), MatchStatus.SCHEDULED,
                null, null, null, null);

        Odds first = new Odds(UUID.randomUUID(), matchId, 8L, "Bet365", OffsetDateTime.now().minusHours(3),
                new BigDecimal("2.40"), null, null, null, null, null, null);
        Odds second = new Odds(UUID.randomUUID(), matchId, 8L, "Bet365", OffsetDateTime.now(),
                new BigDecimal("2.10"), null, null, null, null, null, null);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(oddsRepository.findByMatchId(matchId)).thenReturn(List.of(first, second));

        List<Odds> result = useCase.execute(matchId);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Test: getting odds history for a non-existent match should throw")
    void test_GettingOddsHistoryForNonExistentMatch_ShouldThrow() {
        UUID matchId = UUID.randomUUID();
        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.execute(matchId));
    }
}