package io.github.williamandradesantana.sports.application.team;

import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.venue.Venue;

import java.util.Optional;

public record TeamDetails(Team team, Optional<Venue> venue) {
}
