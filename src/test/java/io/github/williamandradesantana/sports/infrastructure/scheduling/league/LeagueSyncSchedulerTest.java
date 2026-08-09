package io.github.williamandradesantana.sports.infrastructure.scheduling.league;

import io.github.williamandradesantana.sports.application.league.SyncLeagueUseCase;
import io.github.williamandradesantana.sports.application.shared.ExternalDataSourceException;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.TrackedLeaguesProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LeagueSyncSchedulerTest {

    @Mock
    private SyncLeagueUseCase syncLeagueUseCase;

    @Test
    @DisplayName("Test: syncing tracked leagues should call the use case for each configured external id")
    void test_SyncingTrackedLeagues_ShouldCallUseCaseForEachId() {
        var scheduler = new LeagueSyncScheduler(syncLeagueUseCase, new TrackedLeaguesProperties(List.of(39L, 140L)));
        scheduler.syncTrackedLeagues();

        verify(syncLeagueUseCase).syncByExternalId(39L);
        verify(syncLeagueUseCase).syncByExternalId(140L);
    }

    @Test
    @DisplayName("Test: a failure syncing one league should not prevent syncing the next")
    void test_FailureOnOneLeague_ShouldNotPreventNext() {
        var scheduler = new LeagueSyncScheduler(syncLeagueUseCase, new TrackedLeaguesProperties(List.of(39L, 140L)));

        doThrow(new ExternalDataSourceException("API down", new RuntimeException()))
                .when(syncLeagueUseCase).syncByExternalId(39L);

        scheduler.syncTrackedLeagues();

        verify(syncLeagueUseCase).syncByExternalId(39L);
        verify(syncLeagueUseCase).syncByExternalId(140L);
    }
}