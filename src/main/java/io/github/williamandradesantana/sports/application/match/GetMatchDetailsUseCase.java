package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.Venue;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;

import java.util.Optional;
import java.util.UUID;

public class GetMatchDetailsUseCase {

    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;
    private final MatchRepository matchRepository;

    public GetMatchDetailsUseCase(TeamRepository teamRepository, VenueRepository venueRepository, MatchRepository matchRepository) {
        this.teamRepository = teamRepository;
        this.venueRepository = venueRepository;
        this.matchRepository = matchRepository;
    }

    public MatchDetails execute(UUID id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found: " + id));
        return toDetails(match);
    }

    public MatchDetails toDetails(Match match) {
        Team homeTeam = teamRepository.findById(match.getHomeTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Home team not found: " + match.getHomeTeamId()));
        Team awayTeam = teamRepository.findById(match.getAwayTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Home team not found: " + match.getAwayTeamId()));
        Optional<Venue> venue = match.getVenueId().flatMap(venueRepository::findById);
        return new MatchDetails(match, homeTeam, awayTeam, venue);
    }
}
