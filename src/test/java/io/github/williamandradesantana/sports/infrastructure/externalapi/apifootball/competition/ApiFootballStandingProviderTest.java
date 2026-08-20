package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition;

import io.github.williamandradesantana.sports.application.competition.ExternalStandingData;
import io.github.williamandradesantana.sports.domain.competition.StandingTrend;
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

class ApiFootballStandingProviderTest {

    private MockRestServiceServer mockServer;
    private ApiFootballStandingProvider provider;

    @BeforeEach
    void setup() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://v3.football.api-sports.io");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        provider = new ApiFootballStandingProvider(new TestApiFootballHttpClient(builder.build()));
    }

    @Test
    @DisplayName("Test: fetching standings with a single group should flatten and translate correctly")
    void test_FetchingSingleGroupStandings_ShouldFlattenAndTranslate() {
        String json = """
            {
              "response": [{
                "league": {
                  "id": 71, "season": 2024,
                  "standings": [[
                    {
                      "rank": 1, "team": {"id": 120, "name": "Botafogo", "logo": "logo.png"},
                      "points": 79, "goalsDiff": 30, "group": "Serie A", "form": "WWWDD",
                      "status": "same", "description": "CONMEBOL Libertadores",
                      "all": {"played": 38, "win": 23, "draw": 10, "lose": 5, "goals": {"for": 59, "against": 29}},
                      "home": {"played": 19, "win": 12, "draw": 5, "lose": 2, "goals": {"for": 31, "against": 13}},
                      "away": {"played": 19, "win": 11, "draw": 5, "lose": 3, "goals": {"for": 28, "against": 16}},
                      "update": "2024-12-11T00:00:00+00:00"
                    },
                    {
                      "rank": 2, "team": {"id": 131, "name": "Corinthians", "logo": "logo.png"},
                      "points": 70, "goalsDiff": 10, "group": "Serie A", "form": "WWDLW",
                      "status": "up", "description": null,
                      "all": {"played": 38, "win": 20, "draw": 10, "lose": 8, "goals": {"for": 50, "against": 40}},
                      "home": {"played": 19, "win": 10, "draw": 5, "lose": 4, "goals": {"for": 25, "against": 20}},
                      "away": {"played": 19, "win": 10, "draw": 5, "lose": 4, "goals": {"for": 25, "against": 20}},
                      "update": "2024-12-11T00:00:00+00:00"
                    }
                  ]]
                }
              }]
            }
            """;

        mockServer.expect(requestTo("https://v3.football.api-sports.io/standings?league=71&season=2024"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<ExternalStandingData> result = provider.fetchByLeagueAndSeason(71L, 2024);

        assertEquals(2, result.size());
        assertEquals(StandingTrend.SAME, result.get(0).trend());
        assertEquals(StandingTrend.UP, result.get(1).trend());
        assertEquals(30, result.get(0).overall().goalDifference());

        mockServer.verify();
    }

    @Test
    @DisplayName("Test: fetching standings with multiple groups should flatten all of them into one list")
    void test_FetchingMultipleGroupStandings_ShouldFlattenAll() {
        String json = """
            {
              "response": [{
                "league": {
                  "id": 2, "season": 2024,
                  "standings": [
                    [{
                      "rank": 1, "team": {"id": 1, "name": "Team A", "logo": "logo.png"},
                      "points": 10, "goalsDiff": 5, "group": "Group A", "form": "WWWWW",
                      "status": "same", "description": null,
                      "all": {"played": 4, "win": 3, "draw": 1, "lose": 0, "goals": {"for": 8, "against": 3}},
                      "home": {"played": 2, "win": 2, "draw": 0, "lose": 0, "goals": {"for": 5, "against": 1}},
                      "away": {"played": 2, "win": 1, "draw": 1, "lose": 0, "goals": {"for": 3, "against": 2}},
                      "update": "2024-12-11T00:00:00+00:00"
                    }],
                    [{
                      "rank": 1, "team": {"id": 2, "name": "Team B", "logo": "logo.png"},
                      "points": 9, "goalsDiff": 3, "group": "Group B", "form": "WWDWW",
                      "status": "down", "description": null,
                      "all": {"played": 4, "win": 3, "draw": 0, "lose": 1, "goals": {"for": 7, "against": 4}},
                      "home": {"played": 2, "win": 2, "draw": 0, "lose": 0, "goals": {"for": 4, "against": 1}},
                      "away": {"played": 2, "win": 1, "draw": 0, "lose": 1, "goals": {"for": 3, "against": 3}},
                      "update": "2024-12-11T00:00:00+00:00"
                    }]
                  ]
                }
              }]
            }
            """;

        mockServer.expect(requestTo("https://v3.football.api-sports.io/standings?league=71&season=2024"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<ExternalStandingData> result = provider.fetchByLeagueAndSeason(71L, 2024);

        assertEquals(2, result.size(), () -> "Expected entries from both groups to be flattened into one list");
        assertEquals("Group A", result.get(0).groupName());
        assertEquals("Group B", result.get(1).groupName());
        assertEquals(StandingTrend.DOWN, result.get(1).trend());

        mockServer.verify();
    }
}