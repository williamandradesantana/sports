package io.github.williamandradesantana.sports.application.match;

import java.util.List;

public interface MatchStatisticsProvider {
    List<ExternalMatchStatisticsData> fetchByMatchExternalId(Long matchExternalId);
}
