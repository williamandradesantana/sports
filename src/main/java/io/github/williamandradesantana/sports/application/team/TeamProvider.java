package io.github.williamandradesantana.sports.application.team;

import java.util.List;

public interface TeamProvider {
    List<ExternalTeamData> fetchTeamByExternalId(Long externalId);
    List<ExternalTeamData> fetchTeamsByLeagueAndSeason(Long leagueExternalId, int season);
}
