package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.league.LeagueType;
import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.match.MatchStatus;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SyncMatchUseCaseTest {

    @Mock
    private MatchProvider matchProvider;
    @Mock private MatchRepository matchRepository;
    @Mock private LeagueRepository leagueRepository;
    @Mock private SeasonRepository seasonRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private VenueRepository venueRepository;

    private SyncMatchUseCase useCase;

    private League league;
    private Season season;
    private Team homeTeam;
    private Team awayTeam;
    private ExternalMatchData externalMatch;

    @BeforeEach
    void setup() {
        useCase = new SyncMatchUseCase(matchProvider, matchRepository, leagueRepository,
                seasonRepository, teamRepository, venueRepository);

        league = new League(UUID.randomUUID(), 39L, "Premier League", LeagueType.LEAGUE, "logo.png",
                new Country("England", "GB-ENG", "flag.svg"));
        season = new Season(UUID.randomUUID(), league.getId(), 2026,
                LocalDate.of(2026, 8, 21), LocalDate.of(2027, 5, 30), true);
        homeTeam = new Team(UUID.randomUUID(), 33L, "Manchester United", "MUN", "England",
                1878, false, "logo.png", null);
        awayTeam = new Team(UUID.randomUUID(), 34L, "Liverpool", "LIV", "England",
                1892, false, "logo.png", null);

        externalMatch = new ExternalMatchData(
                215662L, 39L, 2026, 33L, 34L, null,
                OffsetDateTime.parse("2026-08-21T14:00:00Z"), MatchStatus.SCHEDULED,
                null, null, "Round 1", null
        );
    }

    @Test
    @DisplayName("Test: syncing a new match should resolve all external ids and create it")
    void test_SyncingNewMatch_ShouldResolveIdsAndCreate() {
        when(matchProvider.fetchMatchByExternalId(215662L)).thenReturn(List.of(externalMatch));
        when(leagueRepository.findByExternalId(39L)).thenReturn(Optional.of(league));
        when(seasonRepository.findByLeagueIdAndYear(league.getId(), 2026)).thenReturn(Optional.of(season));
        when(teamRepository.findByExternalId(33L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findByExternalId(34L)).thenReturn(Optional.of(awayTeam));
        when(matchRepository.findByExternalId(215662L)).thenReturn(Optional.empty());

        Match result = useCase.syncByExternalId(215662L);

        assertEquals(homeTeam.getId(), result.getHomeTeamId());
        assertEquals(awayTeam.getId(), result.getAwayTeamId());
        assertEquals(season.getId(), result.getSeasonId());
        verify(matchRepository).save(result);
    }

    @Test
    @DisplayName("Test: syncing when the league was not synced yet should throw")
    void test_SyncingWhenLeagueNotSynced_ShouldThrow() {
        when(matchProvider.fetchMatchByExternalId(215662L)).thenReturn(List.of(externalMatch));
        when(leagueRepository.findByExternalId(39L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.syncByExternalId(215662L));

        verify(matchRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test: syncing when a team was not synced yet should throw")
    void test_SyncingWhenTeamNotSynced_ShouldThrow() {
        when(matchProvider.fetchMatchByExternalId(215662L)).thenReturn(List.of(externalMatch));
        when(leagueRepository.findByExternalId(39L)).thenReturn(Optional.of(league));
        when(seasonRepository.findByLeagueIdAndYear(league.getId(), 2026)).thenReturn(Optional.of(season));
        when(teamRepository.findByExternalId(33L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> useCase.syncByExternalId(215662L));

        verify(matchRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test: syncing with an unresolved venue should still create the match, without a venue")
    void test_SyncingWithUnresolvedVenue_ShouldCreateMatchWithoutVenue() {
        ExternalMatchData withVenue = new ExternalMatchData(
                215662L, 39L, 2026, 33L, 34L, 999L,
                OffsetDateTime.parse("2026-08-21T14:00:00Z"), MatchStatus.SCHEDULED,
                null, null, "Round 1", null
        );

        when(matchProvider.fetchMatchByExternalId(215662L)).thenReturn(List.of(withVenue));
        when(leagueRepository.findByExternalId(39L)).thenReturn(Optional.of(league));
        when(seasonRepository.findByLeagueIdAndYear(league.getId(), 2026)).thenReturn(Optional.of(season));
        when(teamRepository.findByExternalId(33L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findByExternalId(34L)).thenReturn(Optional.of(awayTeam));
        when(venueRepository.findByExternalId(999L)).thenReturn(Optional.empty());
        when(matchRepository.findByExternalId(215662L)).thenReturn(Optional.empty());

        Match result = useCase.syncByExternalId(215662L);

        assertTrue(result.getVenueId().isEmpty());
    }

    @Test
    @DisplayName("Test: syncing an existing match should update it, not create a duplicate")
    void test_SyncingExistingMatch_ShouldUpdateNotDuplicate() {
        Match existingMatch = new Match(UUID.randomUUID(), 215662L, league.getId(), season.getId(),
                homeTeam.getId(), awayTeam.getId(), null,
                OffsetDateTime.parse("2026-08-21T14:00:00Z"), MatchStatus.SCHEDULED, null, null, "Round 1", null);

        ExternalMatchData finishedMatch = new ExternalMatchData(
                215662L, 39L, 2026, 33L, 34L, null,
                OffsetDateTime.parse("2026-08-21T16:00:00Z"), MatchStatus.FINISHED,
                2, 1, "Round 1", "H. Mastrángelo"
        );

        when(matchProvider.fetchMatchByExternalId(215662L)).thenReturn(List.of(finishedMatch));
        when(leagueRepository.findByExternalId(39L)).thenReturn(Optional.of(league));
        when(seasonRepository.findByLeagueIdAndYear(league.getId(), 2026)).thenReturn(Optional.of(season));
        when(teamRepository.findByExternalId(33L)).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findByExternalId(34L)).thenReturn(Optional.of(awayTeam));
        when(matchRepository.findByExternalId(215662L)).thenReturn(Optional.of(existingMatch));

        Match result = useCase.syncByExternalId(215662L);

        assertEquals(existingMatch.getId(), result.getId(), () -> "Expected same id, not a new one");
        assertEquals(MatchStatus.FINISHED, result.getStatus());
        assertEquals(homeTeam.getId(), result.getWinnerId().orElseThrow());
    }
}