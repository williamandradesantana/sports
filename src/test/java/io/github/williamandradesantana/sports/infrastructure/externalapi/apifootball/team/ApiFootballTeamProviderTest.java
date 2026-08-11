package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.team;

import io.github.williamandradesantana.sports.application.team.ExternalTeamData;
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

class ApiFootballTeamProviderTest {

    private MockRestServiceServer mockServer;
    private ApiFootballTeamProvider provider;

    @BeforeEach
    void setup() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://v3.football.api-sports.io");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        provider = new ApiFootballTeamProvider(new TestApiFootballHttpClient(builder.build()));
    }

    @Test
    @DisplayName("Test: fetching a team by external id should translate the API response correctly")
    void test_FetchingTeamByExternalId_ShouldTranslateCorrectly() {
        String json = """
            {
              "response": [{
                "team": {"id": 33, "name": "Manchester United", "code": "MUN", "country": "England",
                         "founded": 1878, "national": false, "logo": "https://logo.png"},
                "venue": {"id": 556, "name": "Old Trafford", "address": "Sir Matt Busby Way",
                          "city": "Manchester", "capacity": 76212, "surface": "grass", "image": "https://image.png"}
              }]
            }
            """;

        mockServer.expect(requestTo("https://v3.football.api-sports.io/teams?id=33"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<ExternalTeamData> result = provider.fetchTeamByExternalId(33L);

        assertEquals(1, result.size());
        ExternalTeamData team = result.get(0);
        assertEquals("Manchester United", team.name());
        assertNotNull(team.venue());
        assertEquals("Old Trafford", team.venue().name());

        mockServer.verify();
    }

    @Test
    @DisplayName("Test: fetching a national team should translate with a null venue")
    void test_FetchingNationalTeam_ShouldTranslateWithNullVenue() {
        String json = """
            {
              "response": [{
                "team": {"id": 10, "name": "Brazil", "code": "BRA", "country": "Brazil",
                         "founded": null, "national": true, "logo": "https://logo.png"}
              }]
            }
            """;

        mockServer.expect(requestTo("https://v3.football.api-sports.io/teams?id=10"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<ExternalTeamData> result = provider.fetchTeamByExternalId(10L);

        assertNull(result.get(0).venue());

        mockServer.verify();
    }

    @Test
    @DisplayName("Test: fetching teams by league and season should build the correct query")
    void test_FetchingTeamsByLeagueAndSeason_ShouldBuildCorrectQuery() {
        mockServer.expect(requestTo("https://v3.football.api-sports.io/teams?league=39&season=2026"))
                .andRespond(withSuccess("{\"response\": []}", MediaType.APPLICATION_JSON));

        provider.fetchTeamsByLeagueAndSeason(39L, 2026);

        mockServer.verify();
    }
}