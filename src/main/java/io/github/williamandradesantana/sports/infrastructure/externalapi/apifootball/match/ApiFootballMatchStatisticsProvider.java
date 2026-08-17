package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match;

import io.github.williamandradesantana.sports.application.match.ExternalMatchStatisticsData;
import io.github.williamandradesantana.sports.application.match.MatchStatisticsProvider;
import io.github.williamandradesantana.sports.application.shared.ExternalDataSourceException;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto.FixtureStatisticsApiResponse;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto.FixtureStatisticsResponseItem;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto.StatisticEntryDto;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiFootballMatchStatisticsProvider implements MatchStatisticsProvider {

    private final ApiFootballHttpClient httpClient;

    public ApiFootballMatchStatisticsProvider(ApiFootballHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<ExternalMatchStatisticsData> fetchByMatchExternalId(Long matchExternalId) {
        try {
            FixtureStatisticsApiResponse response = httpClient.getRestClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/fixtures/statistics")
                        .queryParam("fixture", matchExternalId).build())
                .retrieve()
                .body(FixtureStatisticsApiResponse.class);
            return toExternalDataList(response);
        } catch (RestClientException e) {
            throw new ExternalDataSourceException("Failed to fetch match statistics from API-Football", e);
        }
    }

    private List<ExternalMatchStatisticsData> toExternalDataList(FixtureStatisticsApiResponse response) {
        if (response == null || response.response() == null) return List.of();
        return response.response().stream().map(this::toExternalData).toList();
    }

    private ExternalMatchStatisticsData toExternalData(FixtureStatisticsResponseItem item) {
        Map<String, Object> byType = new HashMap<>();
        for (StatisticEntryDto entry : item.statistics()) {
            byType.put(entry.type(), entry.value());
        }

        return new ExternalMatchStatisticsData(
            item.team().id(),
            asInteger(byType.get("Shots on Goal")),
            asInteger(byType.get("Shots off Goal")),
            asInteger(byType.get("Total Shots")),
            asInteger(byType.get("Blocked Shots")),
            asInteger(byType.get("Shots insidebox")),
            asInteger(byType.get("Shots outsidebox")),
            asInteger(byType.get("Fouls")),
            asInteger(byType.get("Corner Kicks")),
            asInteger(byType.get("Offsides")),
            asInteger(byType.get("Ball Possession")),
            asInteger(byType.get("Yellow Cards")),
            asInteger(byType.get("Red Cards")),
            asInteger(byType.get("Goalkeeper Saves")),
            asInteger(byType.get("Total passes")),
            asInteger(byType.get("Passes accurate")),
            asInteger(byType.get("Passes %"))
        );
    }

    private Integer asInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer intValue) {
            return intValue;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String stringValue) {
            String digitsOnly = stringValue.replaceAll("[^0-9-]", "");
            if (digitsOnly.isBlank()) {
                return null;
            }
            try {
                return Integer.parseInt(digitsOnly);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}
