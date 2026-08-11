package io.github.williamandradesantana.sports.application.league;

import io.github.williamandradesantana.sports.domain.competition.Season;
import io.github.williamandradesantana.sports.domain.league.League;

import java.util.List;

public record LeagueDetails(League league, List<Season> seasons) {
}
