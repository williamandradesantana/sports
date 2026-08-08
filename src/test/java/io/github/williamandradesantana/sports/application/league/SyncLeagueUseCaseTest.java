package io.github.williamandradesantana.sports.application.league;

import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncLeagueUseCaseTest {

    @Mock
    private LeagueRepository leagueRepository;

    @Mock
    private SeasonRepository seasonRepository;

    @Mock
    private LeagueProvider leagueProvider;

    private SyncLeagueUseCase syncLeagueUseCase;
    private ExternalLeagueData externalLeague;
    private Country country;

    @BeforeEach
    void setUp() {
        syncLeagueUseCase = new SyncLeagueUseCase(leagueProvider, leagueRepository, seasonRepository);
        country = new Country("England", "GB-ENG", "https://flag.svg");
        externalLeague = new ExternalLeagueData(
            39L, "Premier League", LeagueType.LEAGUE, "https://logo.svg", country,
            List.of(new ExternalSeasonData(
                2026,
                LocalDate.of(2026, 8, 21),
                LocalDate.of(2027, 5, 30),
                true
            ))
        );
    }

    @Test
    @DisplayName("Test: syncing a new league should create it and its seasons")
    void test_SyncingNewLeague_ShouldCreateLeagueAndSeasons() {
        when(leagueProvider.fetchLeagueByExternalId(externalLeague.externalId())).thenReturn(List.of(externalLeague));
        when(leagueRepository.findByExternalId(39L)).thenReturn(Optional.empty());
        when(seasonRepository.findByLeagueIdAndYear(any(), eq(2026))).thenReturn(Optional.empty());

        syncLeagueUseCase.syncByExternalId(externalLeague.externalId());

        ArgumentCaptor<League> leagueCaptor = ArgumentCaptor.forClass(League.class);
        verify(leagueRepository).save(leagueCaptor.capture());
        assertEquals("Premier League", leagueCaptor.getValue().getName());

        ArgumentCaptor<Season> seasonCaptor = ArgumentCaptor.forClass(Season.class);
        verify(seasonRepository).save(seasonCaptor.capture());
        assertEquals(2026, seasonCaptor.getValue().getYear());
        assertEquals(leagueCaptor.getValue().getId(), seasonCaptor.getValue().getLeagueId());
    }

    @Test
    @DisplayName("Test: syncing an existing league should update it, not create a duplicate")
    void test_SyncingExistingLeague_ShouldUpdateNotDuplicate() {
        League existingLeague = new League(
                UUID.randomUUID(), 39L,
                "Premier League (old name)", LeagueType.LEAGUE, "old-logo.png", country
        );
        ExternalLeagueData external = new ExternalLeagueData(
                39L, "Premier League",
                LeagueType.LEAGUE, "https://new-logo.png", country, List.of()
        );

        when(leagueProvider.fetchLeagueByExternalId(externalLeague.externalId())).thenReturn(List.of(external));
        when(leagueRepository.findByExternalId(existingLeague.getExternalId())).thenReturn(Optional.of(existingLeague));

        syncLeagueUseCase.syncByExternalId(39L);

        ArgumentCaptor<League> captor = ArgumentCaptor.forClass(League.class);
        verify(leagueRepository).save(captor.capture());
        assertEquals(existingLeague.getId(), captor.getValue().getId(), () -> "Expected the same id, not a new one");
        assertEquals("Premier League", captor.getValue().getName());
        assertEquals("https://new-logo.png", captor.getValue().getLogoUrl());
    }

    @Test
    @DisplayName("Test: syncing an existing season should update current flag, not create a duplicate")
    void test_SyncingExistingSeason_ShouldUpdateNotDuplicate() {
        UUID leagueId = UUID.randomUUID();
        League league = new League(leagueId, 39L, "Premier League", LeagueType.LEAGUE, "logo.png", country);
        Season existingSeason = new Season(
                UUID.randomUUID(), leagueId, 2025,
                LocalDate.of(2025, 8, 15), LocalDate.of(2026, 5, 24), true
        );

        ExternalLeagueData external = new ExternalLeagueData(
                39L, "Premier League", LeagueType.LEAGUE, "logo.png", country,
            List.of(new ExternalSeasonData(
                2025,
                LocalDate.of(2025, 8, 15),
                LocalDate.of(2026, 5, 24), false)
            )
        );

        when(leagueProvider.fetchLeagueByExternalId(39L)).thenReturn(List.of(external));
        when(leagueRepository.findByExternalId(39L)).thenReturn(Optional.of(league));
        when(seasonRepository.findByLeagueIdAndYear(leagueId, 2025)).thenReturn(Optional.of(existingSeason));

        syncLeagueUseCase.syncByExternalId(39L);

        ArgumentCaptor<Season> captor = ArgumentCaptor.forClass(Season.class);
        verify(seasonRepository).save(captor.capture());
        assertEquals(existingSeason.getId(), captor.getValue().getId());
        assertFalse(captor.getValue().isCurrent(), () -> "Expected season to be marked as no longer current");
    }

    @Test
    @DisplayName("Test: league is saved before its seasons, respecting the foreign key")
    void test_LeagueSavedBeforeSeasons() {
        ExternalLeagueData external = new ExternalLeagueData(
            39L, "Premier League", LeagueType.LEAGUE, "logo.png", country,
            List.of(new ExternalSeasonData(
                    2026, LocalDate.of(2026, 8, 21),
                    LocalDate.of(2027, 5, 30), true)
            )
        );

        when(leagueProvider.fetchLeagueByExternalId(39L)).thenReturn(List.of(external));
        when(leagueRepository.findByExternalId(39L)).thenReturn(Optional.empty());
        when(seasonRepository.findByLeagueIdAndYear(any(), anyInt())).thenReturn(Optional.empty());

        InOrder inOrder = inOrder(leagueRepository, seasonRepository);

        syncLeagueUseCase.syncByExternalId(39L);

        inOrder.verify(leagueRepository).save(any());
        inOrder.verify(seasonRepository).save(any());
    }
}