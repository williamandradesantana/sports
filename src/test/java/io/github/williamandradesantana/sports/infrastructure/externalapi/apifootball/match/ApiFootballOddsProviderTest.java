package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match;

import io.github.williamandradesantana.sports.application.match.ExternalOddsData;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.shared.TestApiFootballHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ApiFootballOddsProviderTest {

    private MockRestServiceServer mockServer;
    private ApiFootballOddsProvider provider;

    @BeforeEach
    void setup() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://v3.football.api-sports.io");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        provider = new ApiFootballOddsProvider(new TestApiFootballHttpClient(builder.build()));
    }

    @Test
    @DisplayName("Test: fetching odds should extract Match Winner, Goals Over/Under 2.5 and Both Teams Score")
    void test_FetchingOdds_ShouldExtractTheThreeMarkets() {
        String json = """
            {
              "response": [{
                "fixture": {"id": 1490391},
                "update": "2026-08-19T22:00:00+00:00",
                "bookmakers": [{
                  "id": 8, "name": "Bet365",
                  "bets": [
                    {"id": 1, "name": "Match Winner", "values": [
                      {"value": "Home", "odd": "2.40"}, {"value": "Draw", "odd": "3.90"}, {"value": "Away", "odd": "2.62"}
                    ]},
                    {"id": 5, "name": "Goals Over/Under", "values": [
                      {"value": "Over 2.5", "odd": "1.40"}, {"value": "Under 2.5", "odd": "2.88"},
                      {"value": "Over 1.5", "odd": "1.10"}
                    ]},
                    {"id": 8, "name": "Both Teams Score", "values": [
                      {"value": "Yes", "odd": "1.40"}, {"value": "No", "odd": "2.75"}
                    ]}
                  ]
                }]
              }]
            }
            """;

        mockServer.expect(requestTo("https://v3.football.api-sports.io/odds?fixture=1490391&bookmaker=8"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        Optional<ExternalOddsData> result = provider.fetchByMatchAndBookmaker(1490391L, 8L);

        assertTrue(result.isPresent());
        ExternalOddsData data = result.get();
        assertEquals(new BigDecimal("2.40"), data.homeWinOdd());
        assertEquals(new BigDecimal("1.40"), data.overGoalsOdd());
        assertEquals(new BigDecimal("2.88"), data.underGoalsOdd());
        assertEquals(new BigDecimal("1.40"), data.bothTeamsScoreYesOdd());

        mockServer.verify();
    }

    @Test
    @DisplayName("Test: a missing market should map to null instead of throwing")
    void test_MissingMarket_ShouldMapToNull() {
        String json = """
            {
              "response": [{
                "fixture": {"id": 1490391},
                "update": "2026-08-19T22:00:00+00:00",
                "bookmakers": [{
                  "id": 8, "name": "Bet365",
                  "bets": [{"id": 1, "name": "Match Winner", "values": [
                    {"value": "Home", "odd": "2.40"}, {"value": "Draw", "odd": "3.90"}, {"value": "Away", "odd": "2.62"}
                  ]}]
                }]
              }]
            }
            """;

        mockServer.expect(requestTo("https://v3.football.api-sports.io/odds?fixture=1490391&bookmaker=8"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        ExternalOddsData data = provider.fetchByMatchAndBookmaker(1490391L, 8L).orElseThrow();

        assertEquals(new BigDecimal("2.40"), data.homeWinOdd());
        assertNull(data.overGoalsOdd());
        assertNull(data.bothTeamsScoreYesOdd());

        mockServer.verify();
    }

    @Test
    @DisplayName("Test: no odds available for the requested bookmaker should return empty")
    void test_NoOddsForBookmaker_ShouldReturnEmpty() {
        mockServer.expect(requestTo("https://v3.football.api-sports.io/odds?fixture=1490391&bookmaker=999"))
                .andRespond(withSuccess("{\"response\": []}", MediaType.APPLICATION_JSON));

        Optional<ExternalOddsData> result = provider.fetchByMatchAndBookmaker(1490391L, 999L);

        assertTrue(result.isEmpty());

        mockServer.verify();
    }
}