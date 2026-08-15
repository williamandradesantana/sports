package io.github.williamandradesantana.sports.application.match;

import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.team.Team;
import io.github.williamandradesantana.sports.domain.venue.Venue;

import java.util.Optional;

public record MatchDetails(Match match, Team homeTeam, Team awayTeam, Optional<Venue> venue) {
}
