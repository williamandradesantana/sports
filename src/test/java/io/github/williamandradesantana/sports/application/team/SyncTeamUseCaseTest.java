package io.github.williamandradesantana.sports.application.team;

import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.Venue;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncTeamUseCaseTest {

    @Mock
    private TeamProvider teamProvider;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private VenueRepository venueRepository;

    private SyncTeamUseCase syncTeamUseCase;
    private ExternalTeamData externalTeam;
    private ExternalVenueData externalVenue;

    @BeforeEach
    void setUp() {
        syncTeamUseCase = new SyncTeamUseCase(teamProvider, teamRepository, venueRepository);
        externalVenue = new ExternalVenueData(
            556L, "Old Trafford", "Sir Matt Busby Way", "Manchester",
            76212, "grass", "https://image.png"
        );
        externalTeam = new ExternalTeamData(
            33L, "Manchester United", "MUN", "England", 1878,
            false, "https://logo.png", externalVenue
        );
    }

    @AfterEach
    void afterEach() {
        externalVenue = null;
        externalTeam = null;
    }

    @Test
    @DisplayName("Test: syncing a new club team should create both venue and team, venue first")
    void test_SyncingNewClubTeam_ShouldCreateVenueBeforeTeam() {
        when(teamProvider.fetchTeamByExternalId(externalTeam.externalId())).thenReturn(List.of(externalTeam));
        when(venueRepository.findByExternalId(externalVenue.externalId())).thenReturn(Optional.empty());
        when(teamRepository.findByExternalId(externalTeam.externalId())).thenReturn(Optional.empty());

        Team result = syncTeamUseCase.syncByExternalId(externalTeam.externalId());
        assertEquals("Manchester United", externalTeam.name());
        assertTrue(result.getVenueId().isPresent());

        InOrder inOrder = inOrder(venueRepository, teamRepository);
        inOrder.verify(venueRepository).save(any());
        inOrder.verify(teamRepository).save(any());
    }

    @Test
    @DisplayName("Test: syncing a national team without venue data should create team with no venue")
    void test_SyncingNationalTeam_ShouldCreateTeamWithoutVenue() {
        ExternalTeamData nationalTeam = new ExternalTeamData(
                10L, "Brazil", "BRA", "Brazil", null, true, "https://logo.png", null
        );
        when(teamProvider.fetchTeamByExternalId(nationalTeam.externalId())).thenReturn(List.of(nationalTeam));
        when(teamRepository.findByExternalId(nationalTeam.externalId())).thenReturn(Optional.empty());

        Team result = syncTeamUseCase.syncByExternalId(nationalTeam.externalId());

        assertTrue(result.getVenueId().isEmpty());
        verify(venueRepository, never()).findByExternalId(any());
        verify(venueRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test: syncing an existing team should update it, not create a duplicate")
    void test_SyncingExistingTeam_ShouldUpdateNotDuplicate() {
        Team existingTeam = new Team(UUID.randomUUID(), 33L, "Man United (old)", "MUN",
                "England", 1878, false, "old-logo.png", null);

        ExternalTeamData updatedExternalTeam = new ExternalTeamData(
                33L, "Manchester United", "MUN", "England",
                1878, false, "new-logo.png", null
        );

        when(teamProvider.fetchTeamByExternalId(updatedExternalTeam.externalId())).thenReturn(List.of(updatedExternalTeam));
        when(teamRepository.findByExternalId(updatedExternalTeam.externalId())).thenReturn(Optional.of(existingTeam));

        Team result = syncTeamUseCase.syncByExternalId(existingTeam.getExternalId());

        assertEquals(existingTeam.getId(), result.getId(), () -> "Expected same id, not a new one");
        assertEquals("Manchester United", result.getName());
        assertEquals("new-logo.png", result.getLogoUrl());
    }

    @Test
    @DisplayName("Test: syncing an existing venue should update it, not create a duplicate")
    void test_SyncingExistingVenue_ShouldUpdateNotDuplicate() {
        Venue existingVenue = new Venue(UUID.randomUUID(), 556L, "Old Trafford (old)", "old address",
                "Manchester", 70000, "grass", "old-image.png");

        when(teamProvider.fetchTeamByExternalId(33L)).thenReturn(List.of(externalTeam));
        when(venueRepository.findByExternalId(556L)).thenReturn(Optional.of(existingVenue));
        when(teamRepository.findByExternalId(33L)).thenReturn(Optional.empty());

        syncTeamUseCase.syncByExternalId(33L);

        ArgumentCaptor<Venue> captor = ArgumentCaptor.forClass(Venue.class);
        verify(venueRepository).save(captor.capture());
        assertEquals(existingVenue.getId(), captor.getValue().getId());
        assertEquals(76212, captor.getValue().getCapacity());
    }

    @Test
    @DisplayName("Test: syncing teams by league and season should sync all returned teams")
    void test_SyncingByLeagueAndSeason_ShouldSyncAllTeams() {
        ExternalTeamData team2 = new ExternalTeamData(50L, "Manchester City", "MCI", "England",
                1880, false, "logo2.png", null);

        when(teamProvider.fetchTeamsByLeagueAndSeason(39L, 2026)).thenReturn(List.of(externalTeam, team2));
        when(teamRepository.findByExternalId(any())).thenReturn(Optional.empty());

        List<Team> result = syncTeamUseCase.syncByLeagueAndSeason(39L, 2026);

        assertEquals(2, result.size());
        verify(teamRepository, times(2)).save(any());
    }
}