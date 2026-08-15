package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.competition.SeasonRepository;
import io.github.williamandradesantana.sports.domain.league.League;
import io.github.williamandradesantana.sports.domain.league.LeagueRepository;
import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.Venue;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;

import java.util.List;
import java.util.UUID;

public class SyncMatchUseCase {

    private final MatchProvider matchProvider;
    private final MatchRepository matchRepository;
    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;

    public SyncMatchUseCase(MatchProvider matchProvider, MatchRepository matchRepository, LeagueRepository leagueRepository, SeasonRepository seasonRepository, TeamRepository teamRepository, VenueRepository venueRepository) {
        this.matchProvider = matchProvider;
        this.matchRepository = matchRepository;
        this.leagueRepository = leagueRepository;
        this.seasonRepository = seasonRepository;
        this.teamRepository = teamRepository;
        this.venueRepository = venueRepository;
    }

    public Match syncByExternalId(Long externalId) {
        List<ExternalMatchData> externalMatches = matchProvider.fetchMatchByExternalId(externalId);

        return externalMatches.stream()
                .map(this::syncMatch)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Match not found in external source: externalId=" + externalId));
    }

    public List<Match> syncByLeagueAndSeason(Long leagueExternalId, int season) {
        List<ExternalMatchData> externalMatches = matchProvider.fetchMatchesByLeagueAndSeason(leagueExternalId, season);

        return externalMatches.stream().map(this::syncMatch).toList();
    }

    public List<Match> syncByExternalIds(List<Long> externalIds) {
        List<ExternalMatchData> externalMatches = matchProvider.fetchMatchesByExternalIds(externalIds);
        return externalMatches.stream().map(this::syncMatch).toList();
    }

    private Match syncMatch(ExternalMatchData data) {
        League league = leagueRepository.findByExternalId(data.leagueExternalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "League not synced yet: externalId=" + data.leagueExternalId()));

        Season season = seasonRepository.findByLeagueIdAndYear(league.getId(), data.seasonYear())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Season not synced yet: league=" + data.leagueExternalId() + ", year=" + data.seasonYear()));

        Team homeTeam = teamRepository.findByExternalId(data.homeTeamExternalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Home team not synced yet: externalId=" + data.homeTeamExternalId()));

        Team awayTeam = teamRepository.findByExternalId(data.awayTeamExternalId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Away team not synced yet: externalId=" + data.awayTeamExternalId()));

        UUID venueId = resolveVenueId(data.venueExternalId());

        Match match = matchRepository.findByExternalId(data.externalId())
                .map(existing -> updateExisting(existing, data, venueId))
                .orElseGet(() -> createNew(data, league.getId(), season.getId(),
                        homeTeam.getId(), awayTeam.getId(), venueId));

        matchRepository.save(match);
        return match;
    }

    private UUID resolveVenueId(Long venueExternalId) {
        if (venueExternalId == null) return null;
        return venueRepository.findByExternalId(venueExternalId)
                .map(Venue::getId)
                .orElse(null);
    }

    private Match updateExisting(Match match, ExternalMatchData data, UUID venueId) {
        match.updateFromExternalSource(
                venueId, data.matchDate(), data.status(), data.homeGoals(),
                data.awayGoals(), data.round(), data.referee()
        );
        return match;
    }

    private Match createNew(
            ExternalMatchData data, UUID leagueId, UUID seasonId, UUID homeTeamId, UUID awayTeamId, UUID venueId
    ) {
        return new Match(
            UUID.randomUUID(), data.externalId(), leagueId, seasonId, homeTeamId, awayTeamId, venueId,
            data.matchDate(), data.status(), data.homeGoals(), data.awayGoals(), data.round(), data.referee()
        );
    }
}
