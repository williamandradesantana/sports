package io.github.williamandradesantana.sports.application.team;

import io.github.williamandradesantana.sports.application.shared.ResourceNotFoundException;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.team.TeamRepository;
import io.github.williamandradesantana.sports.domain.venue.Venue;
import io.github.williamandradesantana.sports.domain.venue.VenueRepository;

import java.util.List;
import java.util.UUID;

public class SyncTeamUseCase {

    private final TeamProvider teamProvider;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;

    public SyncTeamUseCase(TeamProvider teamProvider, TeamRepository teamRepository, VenueRepository venueRepository) {
        this.teamProvider = teamProvider;
        this.teamRepository = teamRepository;
        this.venueRepository = venueRepository;
    }

    public Team syncByExternalId(Long externalId) {
        List<ExternalTeamData> externalTeams = teamProvider.fetchTeamByExternalId(externalId);
        return externalTeams.stream()
            .map(this::syncTeam)
            .findFirst()
            .orElseThrow(
                () -> new ResourceNotFoundException("Team not found in external source: externalId=" + externalId)
            );
    }

    public List<Team> syncByLeagueAndSeason(Long leagueExternalId, int season) {
        List<ExternalTeamData> externalTeams = teamProvider.fetchTeamsByLeagueAndSeason(leagueExternalId, season);
        return externalTeams.stream().map(this::syncTeam).toList();
    }

    private Team syncTeam(ExternalTeamData externalTeam) {
        UUID venueId = externalTeam.venue() != null ? syncVenue(externalTeam.venue()) : null;
        Team team = teamRepository.findByExternalId(externalTeam.externalId())
                .map(existing -> updateExistingTeam(existing, externalTeam, venueId))
                .orElseGet(() -> createNewTeam(externalTeam, venueId));
        teamRepository.save(team);
        return team;
    }

    private UUID syncVenue(ExternalVenueData externalVenue) {
        Venue venue = venueRepository.findByExternalId(externalVenue.externalId())
                .map(existing -> updateExistingVenue(existing, externalVenue))
                .orElseGet(() -> createNewVenue(externalVenue));
        venueRepository.save(venue);
        return venue.getId();
    }

    private Venue updateExistingVenue(Venue venue, ExternalVenueData externalVenue) {
        venue.updateFromExternalSource(
            externalVenue.name(), externalVenue.address(), externalVenue.city(), externalVenue.capacity(),
            externalVenue.surface(), externalVenue.imageUrl()
        );
        return venue;
    }

    private Venue createNewVenue(ExternalVenueData externalVenue) {
        return new Venue(
            UUID.randomUUID(), externalVenue.externalId(), externalVenue.name(), externalVenue.address(),
            externalVenue.city(), externalVenue.capacity(), externalVenue.surface(), externalVenue.imageUrl()
        );
    }

    private Team updateExistingTeam(Team team, ExternalTeamData externalTeam, UUID venueId) {
        team.updateFromExternalSource(
            externalTeam.name(), externalTeam.code(), externalTeam.countryName(), externalTeam.founded(),
            externalTeam.logoUrl(), venueId
        );
        return team;
    }

    private Team createNewTeam(ExternalTeamData externalTeam, UUID venueId) {
        return new Team(
            UUID.randomUUID(), externalTeam.externalId(), externalTeam.name(), externalTeam.code(),
            externalTeam.countryName(), externalTeam.founded(), externalTeam.national(), externalTeam.logoUrl(),
            venueId
        );
    }
}
