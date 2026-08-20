package io.github.williamandradesantana.sports.application.competition;

import java.util.List;

public interface StandingProvider {
    List<ExternalStandingData> fetchByLeagueAndSeason(Long leagueExternalId, int season);
}
