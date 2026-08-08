package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.league;

import io.github.williamandradesantana.sports.application.league.ExternalLeagueData;
import io.github.williamandradesantana.sports.application.league.ExternalSeasonData;
import io.github.williamandradesantana.sports.application.league.LeagueProvider;
import io.github.williamandradesantana.sports.application.shared.ExternalDataSourceException;
import io.github.williamandradesantana.sports.domain.league.Country;
import io.github.williamandradesantana.sports.domain.league.LeagueType;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.LeagueResponseItem;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.LeaguesApiResponse;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

public class ApiFootballLeagueProvider implements LeagueProvider {

    private final ApiFootballHttpClient httpClient;

    public ApiFootballLeagueProvider(ApiFootballHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<ExternalLeagueData> fetchAllLeagues() {
        return fetch(uriBuilder -> uriBuilder.path("/leagues").build());
    }

    @Override
    public List<ExternalLeagueData> fetchLeagueByExternalId(Long externalId) {
        return fetch(uriBuilder -> uriBuilder.path("/leagues").queryParam("id", externalId).build());
    }

    private List<ExternalLeagueData> fetch(Function<UriBuilder, URI> uriFunction) {
        try {
            LeaguesApiResponse response = httpClient.getRestClient()
                    .get()
                    .uri(uriFunction)
                    .retrieve()
                    .body(LeaguesApiResponse.class);
            return toExternalLeagueDataList(response);
        } catch (RestClientException e) {
            throw new ExternalDataSourceException("Failed to fetch leagues from API-Football", e);
        }
    }

    private List<ExternalLeagueData> toExternalLeagueDataList(LeaguesApiResponse response) {
        if (response == null || response.response() == null) return List.of();

        return response.response().stream().map(this::toExternalLeagueData).toList();
    }

    private ExternalLeagueData toExternalLeagueData(LeagueResponseItem item) {
        Country country = new Country(item.country().name(), item.country().code(), item.country().flag());
        LeagueType type = "Cup".equalsIgnoreCase(item.league().type()) ? LeagueType.CUP : LeagueType.LEAGUE;

        List<ExternalSeasonData> seasons = item.seasons().stream()
                .map(season ->
                        new ExternalSeasonData(season.year(), season.start(), season.end(), season.current())
                ).toList();
        return new ExternalLeagueData(
                item.league().id(), item.league().name(), type, item.league().logo(), country, seasons
        );
    }
}
