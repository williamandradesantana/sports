package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.match.*;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.TrackedBookmakersProperties;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncOddsUseCaseTest {

    @Mock
    private OddsProvider oddsProvider;

    @Mock
    private OddsRepository oddsRepository;

    @Mock
    private MatchRepository matchRepository;

    private SyncOddsUseCase useCase;
    private Match match;

    @BeforeEach
    void setup() {
        useCase = new SyncOddsUseCase(oddsProvider, oddsRepository, matchRepository,
                new TrackedBookmakersProperties(List.of(8L)));

        match = new Match(UUID.randomUUID(), 1490391L, UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), UUID.randomUUID(), null, OffsetDateTime.now(), MatchStatus.SCHEDULED,
                null, null, null, null);
    }

    @Test
    @DisplayName("Test: syncing odds should create a new snapshot, never update an existing one")
    void test_SyncingOdds_ShouldAlwaysCreateNewSnapshot() {
        ExternalOddsData data = new ExternalOddsData(8L, "Bet365", OffsetDateTime.now(),
                new BigDecimal("2.40"), new BigDecimal("3.90"), new BigDecimal("2.62"),
                new BigDecimal("1.40"), new BigDecimal("2.88"), new BigDecimal("1.40"), new BigDecimal("2.75"));

        when(matchRepository.findByExternalId(1490391L)).thenReturn(Optional.of(match));
        when(oddsProvider.fetchByMatchAndBookmaker(1490391L, 8L)).thenReturn(Optional.of(data));

        useCase.syncByMatchExternalId(1490391L);
        useCase.syncByMatchExternalId(1490391L);

        verify(oddsRepository, times(2)).save(any());
        verify(oddsRepository, never()).findLatestByMatchId(any());
        verifyNoMoreInteractions(oddsRepository);
    }

    @Test
    @DisplayName("Test: syncing odds when the match was not synced yet should throw")
    void test_SyncingWhenMatchNotSynced_ShouldThrow() {
        when(matchRepository.findByExternalId(1490391L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.syncByMatchExternalId(1490391L));

        verifyNoInteractions(oddsProvider, oddsRepository);
    }

    @Test
    @DisplayName("Test: a bookmaker with no odds available should be skipped without failing the sync")
    void test_BookmakerWithNoOdds_ShouldBeSkipped() {
        when(matchRepository.findByExternalId(1490391L)).thenReturn(Optional.of(match));
        when(oddsProvider.fetchByMatchAndBookmaker(1490391L, 8L)).thenReturn(Optional.empty());

        List<Odds> result = useCase.syncByMatchExternalId(1490391L);

        assertTrue(result.isEmpty());
        verify(oddsRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test: syncing should query only the tracked bookmakers")
    void test_SyncingOdds_ShouldQueryOnlyTrackedBookmakers() {
        useCase = new SyncOddsUseCase(oddsProvider, oddsRepository, matchRepository,
                new TrackedBookmakersProperties(List.of(8L, 7L)));

        when(matchRepository.findByExternalId(1490391L)).thenReturn(Optional.of(match));
        when(oddsProvider.fetchByMatchAndBookmaker(eq(1490391L), any())).thenReturn(Optional.empty());

        useCase.syncByMatchExternalId(1490391L);

        verify(oddsProvider).fetchByMatchAndBookmaker(1490391L, 8L);
        verify(oddsProvider).fetchByMatchAndBookmaker(1490391L, 7L);
    }
}