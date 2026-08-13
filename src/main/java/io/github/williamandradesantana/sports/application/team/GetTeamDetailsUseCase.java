package io.github.williamandradesantana.sports.application.team;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.Venue;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;

import java.util.Optional;
import java.util.UUID;

public class GetTeamDetailsUseCase {

    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;

    public GetTeamDetailsUseCase(TeamRepository teamRepository, VenueRepository venueRepository) {
        this.teamRepository = teamRepository;
        this.venueRepository = venueRepository;
    }

    public TeamDetails execute(UUID id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: " + id));

        Optional<Venue> venue = team.getVenueId().flatMap(venueRepository::findById);
        return new TeamDetails(team, venue);
    }
}
