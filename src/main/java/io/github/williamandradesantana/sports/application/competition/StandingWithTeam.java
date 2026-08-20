package io.github.williamandradesantana.sports.application.competition;

import io.github.williamandradesantana.sports.domain.competition.Standing;
import io.github.williamandradesantana.sports.domain.team.Team;

public record StandingWithTeam(Standing standing, Team team) {
}
