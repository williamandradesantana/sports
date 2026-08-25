package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match;

import io.github.williamandradesantana.sports.application.match.ExternalOddsData;
import io.github.williamandradesantana.sports.application.match.OddsProvider;
import io.github.williamandradesantana.sports.application.shared.ExternalDataSourceException;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto.OddsApiResponse;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto.OddsBetDto;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto.OddsBookmakerDto;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto.OddsResponseItem;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.util.Optional;

public class ApiFootballOddsProvider implements OddsProvider {

    private static final int MATCH_WINNER_BET_ID = 1;
    private static final int GOALS_OVER_UNDER_BET_ID = 5;
    private static final int BOTH_TEAMS_SCORE_BET_ID = 8;

    private final ApiFootballHttpClient httpClient;

    public ApiFootballOddsProvider(ApiFootballHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Optional<ExternalOddsData> fetchByMatchAndBookmaker(Long matchExternalId, Long bookmakerExternalId) {
        try {
            OddsApiResponse response = httpClient.getRestClient()
                    .get()
                    .uri(uriBuilder -> uriBuilder.path("/odds")
                            .queryParam("fixture", matchExternalId)
                            .queryParam("bookmaker", bookmakerExternalId)
                            .build())
                    .retrieve()
                    .body(OddsApiResponse.class);

            return toExternalData(response);
        } catch (RestClientException e) {
            throw new ExternalDataSourceException("Failed to fetch odds from API-Football", e);
        }
    }

    private Optional<ExternalOddsData> toExternalData(OddsApiResponse response) {
        if (response == null || response.response() == null || response.response().isEmpty()) return Optional.empty();

        OddsResponseItem item = response.response().get(0);
        if (item.bookmakers() == null || item.bookmakers().isEmpty()) return Optional.empty();

        OddsBookmakerDto bookmaker = item.bookmakers().get(0);

        OddsBetDto matchWinner = findBet(bookmaker, MATCH_WINNER_BET_ID);
        OddsBetDto goalsOverUnder = findBet(bookmaker, GOALS_OVER_UNDER_BET_ID);
        OddsBetDto bothTeamsScore = findBet(bookmaker, BOTH_TEAMS_SCORE_BET_ID);

        return Optional.of(new ExternalOddsData(
            bookmaker.id(), bookmaker.name(), item.update(), findValue(matchWinner, "Home"),
            findValue(matchWinner, "Draw"), findValue(matchWinner, "Away"),
            findValue(goalsOverUnder, "Over 2.5"), findValue(goalsOverUnder, "Under 2.5"),
            findValue(bothTeamsScore, "Yes"), findValue(bothTeamsScore, "No")
        ));
    }

    private OddsBetDto findBet(OddsBookmakerDto bookmaker, int betId) {
        if (bookmaker.bets() == null) return null;
        return bookmaker.bets().stream()
                .filter(bet -> bet.id() == betId)
                .findFirst()
                .orElse(null);
    }

    private BigDecimal findValue(OddsBetDto bet, String valueLabel) {
        if (bet == null || bet.values() == null) return null;
        return bet.values().stream().filter(value -> valueLabel.equalsIgnoreCase(value.value()))
                .findFirst()
                .map(value -> parseOdd(value.odd()))
                .orElse(null);
    }

    private BigDecimal parseOdd(String odd) {
        if (odd == null || odd.isBlank()) return null;

        try {
            return new BigDecimal(odd);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
