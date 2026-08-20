package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition;

import io.github.williamandradesantana.sports.application.competition.ExternalStandingData;
import io.github.williamandradesantana.sports.application.competition.StandingProvider;
import io.github.williamandradesantana.sports.application.shared.ExternalDataSourceException;
import io.github.williamandradesantana.sports.domain.competition.StandingRecord;
import io.github.williamandradesantana.sports.domain.competition.StandingTrend;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition.dto.StandingEntryDto;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition.dto.StandingRecordDto;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition.dto.StandingsApiResponse;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition.dto.StandingsLeagueDto;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

public class ApiFootballStandingProvider implements StandingProvider {

    private final ApiFootballHttpClient httpClient;

    public ApiFootballStandingProvider(ApiFootballHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public List<ExternalStandingData> fetchByLeagueAndSeason(Long leagueExternalId, int season) {
        try {
            StandingsApiResponse response = httpClient.getRestClient()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/standings")
                    .queryParam("league", leagueExternalId)
                    .queryParam("season", season)
                    .build())
                .retrieve()
                .body(StandingsApiResponse.class);

            return toExternalDataList(response);
        } catch (RestClientException e) {
            throw new ExternalDataSourceException("Failed to fetch standings from API-Football", e);
        }
    }

    private List<ExternalStandingData> toExternalDataList(StandingsApiResponse response) {
        if (response == null || response.response() == null || response.response().isEmpty()) {
            return List.of();
        }

        StandingsLeagueDto league = response.response().get(0).league();
        if (league.standings() == null) {
            return List.of();
        }

        return league.standings().stream().flatMap(List::stream).map(this::toExternalData).toList();
    }

    private ExternalStandingData toExternalData(StandingEntryDto entry) {
        return new ExternalStandingData(
            entry.team().id(), entry.rank(), entry.points(), entry.group(), entry.form(), mapTrend(entry.status()),
            entry.description(), toStandingRecord(entry.all()), toStandingRecord(entry.home()),
            toStandingRecord(entry.away()), entry.update()
        );
    }

    private StandingRecord toStandingRecord(StandingRecordDto dto) {
        return new StandingRecord(
            dto.played(), dto.win(), dto.draw(), dto.lose(), dto.goals().goalsFor(), dto.goals().against()
        );
    }

    private StandingTrend mapTrend(String status) {
        if (status == null) return StandingTrend.SAME;
        return switch (status.toLowerCase()) {
            case "up" -> StandingTrend.UP;
            case "down" -> StandingTrend.DOWN;
            default -> StandingTrend.SAME;
        };
    }
}
