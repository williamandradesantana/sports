package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match;

import io.github.williamandradesantana.sports.application.match.ExternalMatchStatisticsData;
import io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.shared.TestApiFootballHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ApiFootballMatchStatisticsProviderTest {

    private MockRestServiceServer mockServer;
    private ApiFootballMatchStatisticsProvider provider;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://v3.football.api-sports.io");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        provider = new ApiFootballMatchStatisticsProvider(new TestApiFootballHttpClient(builder.build()));
    }

    @Test
    @DisplayName("Test: fetching statistics should parse percentage strings into plain integers")
    void test_FetchingStatistics_ShouldParsePercentageStrings() {
        String json = """
            {
              "response": [{
                "team": {"id": 463, "name": "Aldosivi"},
                "statistics": [
                  {"type": "Shots on Goal", "value": 3},
                  {"type": "Ball Possession", "value": "32%"},
                  {"type": "Passes %", "value": "50%"},
                  {"type": "Red Cards", "value": 1}
                ]
              }]
            }
            """;

        mockServer.expect(requestTo("https://v3.football.api-sports.io/fixtures/statistics?fixture=215662"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<ExternalMatchStatisticsData> result = provider.fetchByMatchExternalId(215662L);

        ExternalMatchStatisticsData data = result.get(0);
        assertEquals(463L, data.teamExternalId());
        assertEquals(3, data.shotsOnGoal());
        assertEquals(32, data.ballPossessionPercentage());
        assertEquals(50, data.passesAccuracyPercentage());
        assertEquals(1, data.redCards());

        mockServer.verify();
    }

    @Test
    @DisplayName("Test: a missing statistic type should map to null instead of throwing")
    void test_MissingStatisticType_ShouldMapToNull() {
        String json = """
            {
              "response": [{
                "team": {"id": 463, "name": "Aldosivi"},
                "statistics": [{"type": "Shots on Goal", "value": 3}]
              }]
            }
            """;

        mockServer.expect(requestTo("https://v3.football.api-sports.io/fixtures/statistics?fixture=1"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        ExternalMatchStatisticsData data = provider.fetchByMatchExternalId(1L).get(0);

        assertEquals(3, data.shotsOnGoal());
        assertNull(data.ballPossessionPercentage());

        mockServer.verify();
    }
}