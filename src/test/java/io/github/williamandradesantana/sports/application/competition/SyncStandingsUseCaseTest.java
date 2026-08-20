package io.github.williamandradesantana.sports.application.competition;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.competition.*;
import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueType;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncStandingsUseCaseTest {

    @Mock
    private StandingProvider standingProvider;

    @Mock
    private StandingRepository standingRepository;

    @Mock
    private LeagueRepository leagueRepository;

    @Mock
    private SeasonRepository seasonRepository;

    @Mock
    private TeamRepository teamRepository;

    private SyncStandingsUseCase useCase;

    private League league;
    private Season season;
    private Team team;
    private StandingRecord sampleRecord;

    @BeforeEach
    void setup() {
        useCase = new SyncStandingsUseCase(standingProvider, standingRepository, teamRepository,
                seasonRepository, leagueRepository);

        league = new League(UUID.randomUUID(), 71L, "Serie A", LeagueType.LEAGUE, "logo.png",
                new Country("Brazil", "BR", "flag.svg"));
        season = new Season(UUID.randomUUID(), league.getId(), 2024,
                LocalDate.of(2024, 4, 1), LocalDate.of(2024, 12, 1), true);
        team = new Team(UUID.randomUUID(), 120L, "Botafogo", "BOT", "Brazil", 1904, false, "logo.png", null);
        sampleRecord = new StandingRecord(38, 23, 10, 5, 59, 29);
    }

    @Test
    @DisplayName("Test: syncing new standings should resolve season/team and create them")
    void test_SyncingNewStandings_ShouldResolveAndCreate() {
        ExternalStandingData data = new ExternalStandingData(120L, 1, 79, "Serie A", "WWWDD",
                StandingTrend.SAME, "CONMEBOL Libertadores", sampleRecord, sampleRecord, sampleRecord,
                OffsetDateTime.now());

        when(leagueRepository.findByExternalId(71L)).thenReturn(Optional.of(league));
        when(seasonRepository.findByLeagueIdAndYear(league.getId(), 2024)).thenReturn(Optional.of(season));
        when(standingProvider.fetchByLeagueAndSeason(71L, 2024)).thenReturn(List.of(data));
        when(teamRepository.findByExternalId(120L)).thenReturn(Optional.of(team));
        when(standingRepository.findBySeasonIdAndTeamId(season.getId(), team.getId())).thenReturn(Optional.empty());

        List<Standing> result = useCase.syncByLeagueAndSeason(71L, 2024);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getRank());
        verify(standingRepository).save(any());
    }

    @Test
    @DisplayName("Test: syncing when the league was not synced yet should throw")
    void test_SyncingWhenLeagueNotSynced_ShouldThrow() {
        when(leagueRepository.findByExternalId(71L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.syncByLeagueAndSeason(71L, 2024));

        verify(standingRepository, never()).save(any());
        verifyNoInteractions(standingProvider);
    }

    @Test
    @DisplayName("Test: syncing when the season was not synced yet should throw")
    void test_SyncingWhenSeasonNotSynced_ShouldThrow() {
        when(leagueRepository.findByExternalId(71L)).thenReturn(Optional.of(league));
        when(seasonRepository.findByLeagueIdAndYear(league.getId(), 2024)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.syncByLeagueAndSeason(71L, 2024));

        verifyNoInteractions(standingProvider);
    }

    @Test
    @DisplayName("Test: syncing an existing standing should update it, not create a duplicate")
    void test_SyncingExistingStanding_ShouldUpdateNotDuplicate() {
        Standing existingStanding = new Standing(UUID.randomUUID(), season.getId(), team.getId(), 3, 60,
                "Serie A", "WLDWL", StandingTrend.DOWN, null, sampleRecord, sampleRecord, sampleRecord,
                OffsetDateTime.now().minusDays(1));

        ExternalStandingData data = new ExternalStandingData(120L, 1, 79, "Serie A", "WWWDD",
                StandingTrend.SAME, "CONMEBOL Libertadores", sampleRecord, sampleRecord, sampleRecord,
                OffsetDateTime.now());

        when(leagueRepository.findByExternalId(71L)).thenReturn(Optional.of(league));
        when(seasonRepository.findByLeagueIdAndYear(league.getId(), 2024)).thenReturn(Optional.of(season));
        when(standingProvider.fetchByLeagueAndSeason(71L, 2024)).thenReturn(List.of(data));
        when(teamRepository.findByExternalId(120L)).thenReturn(Optional.of(team));
        when(standingRepository.findBySeasonIdAndTeamId(season.getId(), team.getId()))
                .thenReturn(Optional.of(existingStanding));

        List<Standing> result = useCase.syncByLeagueAndSeason(71L, 2024);

        assertEquals(existingStanding.getId(), result.get(0).getId(), () -> "Expected same id, not a new one");
        assertEquals(1, result.get(0).getRank());
        assertEquals(StandingTrend.SAME, result.get(0).getTrend());
    }

    @Test
    @DisplayName("Test: syncing when a team was not synced yet should throw and skip that entry")
    void test_SyncingWhenTeamNotSynced_ShouldThrow() {
        ExternalStandingData data = new ExternalStandingData(999L, 1, 79, "Serie A", "WWWDD",
                StandingTrend.SAME, null, sampleRecord, sampleRecord, sampleRecord, OffsetDateTime.now());

        when(leagueRepository.findByExternalId(71L)).thenReturn(Optional.of(league));
        when(seasonRepository.findByLeagueIdAndYear(league.getId(), 2024)).thenReturn(Optional.of(season));
        when(standingProvider.fetchByLeagueAndSeason(71L, 2024)).thenReturn(List.of(data));
        when(teamRepository.findByExternalId(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.syncByLeagueAndSeason(71L, 2024));

        verify(standingRepository, never()).save(any());
    }
}