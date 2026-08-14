package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match;

import io.github.williamandradesantana.sports.application.match.ExternalMatchData;
import io.github.williamandradesantana.sports.application.match.MatchProvider;
import io.github.williamandradesantana.sports.application.shared.ExternalDataSourceException;
import io.github.williamandradesantana.sports.domain.match.MatchStatus;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto.FixtureResponseItem;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto.FixturesApiResponse;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

public class ApiFootballMatchProvider implements MatchProvider {

    private final ApiFootballHttpClient httpClient;

    public ApiFootballMatchProvider(ApiFootballHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<ExternalMatchData> fetchMatchByExternalId(Long externalId) {
        return fetch(uriBuilder -> uriBuilder.path("/fixtures").queryParam("id", externalId).build());
    }

    @Override
    public List<ExternalMatchData> fetchMatchesByLeagueAndSeason(Long leagueExternalId, int season) {
        return fetch(uriBuilder ->
                uriBuilder.path("/fixtures")
                    .queryParam("league", leagueExternalId)
                    .queryParam("season", season)
                    .build()
        );
    }

    private List<ExternalMatchData> fetch(Function<UriBuilder, URI> uriFunction) {
        try {
            FixturesApiResponse response = httpClient.getRestClient()
                    .get()
                    .uri(uriFunction)
                    .retrieve()
                    .body(FixturesApiResponse.class);
            return toExternalMatchDataList(response);
        } catch (RestClientException e) {
            throw new ExternalDataSourceException("Failed to fetch fixtures from API-Football", e);
        }
    }

    private List<ExternalMatchData> toExternalMatchDataList(FixturesApiResponse response) {
        if (response == null || response.response() == null) return List.of();
        return response.response().stream().map(this::toExternalMatchData).toList();
    }

    private ExternalMatchData toExternalMatchData(FixtureResponseItem item) {
        Long venueExternalId = item.fixture().venue() != null ? item.fixture().venue().id() : null;
        MatchStatus status = mapStatus(item.fixture().status().statusShort());

        return new ExternalMatchData(
            item.fixture().id(),
            item.league().id(),
            item.league().season(),
            item.teams().home().id(),
            item.teams().away().id(),
            venueExternalId,
            item.fixture().date(),
            status,
            item.goals().home(),
            item.goals().away(),
            item.league().round(),
            item.fixture().referee()
        );
    }

    private MatchStatus mapStatus(String apiStatusCode) {
        return MatchStatusMapping.STATUS_MAPPING.getOrDefault(apiStatusCode, MatchStatus.TO_BE_DEFINED);
    }
}
