package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.team;

import io.github.williamandradesantana.sports.application.shared.ExternalDataSourceException;
import io.github.williamandradesantana.sports.application.team.ExternalTeamData;
import io.github.williamandradesantana.sports.application.team.ExternalVenueData;
import io.github.williamandradesantana.sports.application.team.TeamProvider;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.team.dto.TeamResponseItem;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.team.dto.TeamsApiResponse;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

public class ApiFootballTeamProvider implements TeamProvider {

    private final ApiFootballHttpClient httpClient;

    public ApiFootballTeamProvider(ApiFootballHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<ExternalTeamData> fetchTeamByExternalId(Long externalId) {
        return fetch(uriBuilder -> uriBuilder.path("/teams").queryParam("id", externalId).build());
    }

    @Override
    public List<ExternalTeamData> fetchTeamsByLeagueAndSeason(Long leagueExternalId, int season) {
        return fetch(uriBuilder -> uriBuilder.path("/teams")
                .queryParam("league", leagueExternalId)
                .queryParam("season", season)
                .build()
        );
    }

    private List<ExternalTeamData> fetch(Function<UriBuilder, URI> uriFunction) {
        try {
            TeamsApiResponse response = httpClient.getRestClient()
                    .get()
                    .uri(uriFunction)
                    .retrieve()
                    .body(TeamsApiResponse.class);
            return toExternalTeamDataList(response);
        } catch (RestClientException e) {
            throw new ExternalDataSourceException("Failed to fetch teams from API-Football", e);
        }
    }

    private List<ExternalTeamData> toExternalTeamDataList(TeamsApiResponse response) {
        if (response == null || response.response() == null) return List.of();
        return response.response().stream().map(this::toExternalTeamData).toList();
    }

    private ExternalTeamData toExternalTeamData(TeamResponseItem item) {
        ExternalVenueData venue = item.venue() == null || item.venue().id() == null
            ? null
            : new ExternalVenueData(
                item.venue().id(), item.venue().name(), item.venue().address(),
                item.venue().city(), item.venue().capacity(),
                item.venue().surface(), item.venue().image()
            );

        return new ExternalTeamData(
            item.team().id(), item.team().name(), item.team().code(), item.team().country(),
            item.team().founded(), item.team().national(), item.team().logo(), venue
        );
    }
}
