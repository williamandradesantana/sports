package io.github.williamandradesantana.sports.application.match;

import java.util.List;

public interface MatchProvider {
    List<ExternalMatchData> fetchMatchByExternalId(Long externalId);
    List<ExternalMatchData> fetchMatchesByLeagueAndSeason(Long leagueExternalId, int season);
}
