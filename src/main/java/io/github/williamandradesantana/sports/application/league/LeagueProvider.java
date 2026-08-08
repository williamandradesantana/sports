package io.github.williamandradesantana.sports.application.league;

import java.util.List;

public interface LeagueProvider {
    List<ExternalLeagueData> fetchAllLeagues();
    List<ExternalLeagueData> fetchLeagueByExternalId(Long externalId);
}
