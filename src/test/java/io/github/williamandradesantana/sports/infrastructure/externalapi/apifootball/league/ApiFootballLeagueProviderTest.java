package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.league;

import io.github.williamandradesantana.sports.application.league.ExternalLeagueData;
import io.github.williamandradesantana.sports.domain.league.LeagueType;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ApiFootballLeagueProviderTest {

    private MockRestServiceServer mockServer;
    private ApiFootballLeagueProvider provider;

    @BeforeEach
    void setup() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://v3.football.api-sports.io");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        provider = new ApiFootballLeagueProvider(new TestApiFootballHttpClient(builder.build()));
    }

    @Test
    @DisplayName("Test: fetching a league by external id should translate the API response correctly")
    void test_FetchingLeagueByExternalId_ShouldTranslateCorrectly() {
        String json = """
            {
              "response": [{
                "league": {"id": 39, "name": "Premier League", "type": "League", "logo": "https://logo.png"},
                "country": {"name": "England", "code": "GB-ENG", "flag": "https://flag.svg"},
                "seasons": [{
                  "year": 2026, "start": "2026-08-21", "end": "2027-05-30", "current": true,
                  "coverage": {
                    "fixtures": {"events": false, "lineups": false, "statistics_fixtures": false, "statistics_players": false},
                    "standings": true, "players": false, "top_scorers": false, "top_assists": false,
                    "top_cards": false, "injuries": false, "predictions": true, "odds": false
                  }
                }]
              }]
            }
            """;

        mockServer.expect(requestTo("https://v3.football.api-sports.io/leagues?id=39"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<ExternalLeagueData> result = provider.fetchLeagueByExternalId(39L);

        assertEquals(1, result.size());
        ExternalLeagueData league = result.get(0);
        assertEquals(39L, league.externalId());
        assertEquals("Premier League", league.name());
        assertEquals(LeagueType.LEAGUE, league.type());
        assertEquals("England", league.country().name());
        assertEquals(1, league.seasons().size());
        assertTrue(league.seasons().get(0).current());

        mockServer.verify();
    }
}