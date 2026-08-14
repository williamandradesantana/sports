package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match;

import io.github.williamandradesantana.sports.application.match.ExternalMatchData;
import io.github.williamandradesantana.sports.domain.match.MatchStatus;
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

class ApiFootballMatchProviderTest {

    private MockRestServiceServer mockServer;
    private ApiFootballMatchProvider provider;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("https://v3.football.api-sports.io");
        mockServer = MockRestServiceServer.bindTo(builder).build();
        provider = new ApiFootballMatchProvider(new TestApiFootballHttpClient(builder.build()));
    }

    @Test
    @DisplayName("Test: fetching a finished match should translate status and goals correctly")
    void test_FetchingFinishedMatch_ShouldTranslateCorrectly() {
        String json = """
            {
              "response": [{
                "fixture": {"id": 215662, "referee": "H. Mastrángelo", "date": "2019-10-20T14:00:00+00:00",
                            "venue": {"id": 33, "name": "Estadio", "city": "Mar del Plata"},
                            "status": {"long": "Match Finished", "short": "FT", "elapsed": 90}},
                "league": {"id": 128, "name": "Liga", "country": "Argentina", "season": 2019, "round": "Round 10"},
                "teams": {"home": {"id": 463, "name": "Aldosivi", "winner": true},
                          "away": {"id": 442, "name": "Defensa", "winner": false}},
                "goals": {"home": 1, "away": 0}
              }]
            }
            """;

        mockServer.expect(requestTo("https://v3.football.api-sports.io/fixtures?id=215662"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<ExternalMatchData> result = provider.fetchMatchByExternalId(215662L);

        assertEquals(1, result.size());
        ExternalMatchData match = result.get(0);
        assertEquals(MatchStatus.FINISHED, match.status());
        assertEquals(1, match.homeGoals());
        assertEquals(33L, match.venueExternalId());

        mockServer.verify();
    }

    @Test
    @DisplayName("Test: an unrecognized status code should map to TO_BE_DEFINED instead of throwing")
    void test_UnrecognizedStatusCode_ShouldMapToToBeDefined() {
        String json = """
            {
              "response": [{
                "fixture": {"id": 1, "date": "2026-08-21T14:00:00+00:00",
                            "status": {"long": "Something New", "short": "XYZ", "elapsed": null}},
                "league": {"id": 39, "name": "PL", "country": "England", "season": 2026, "round": "Round 1"},
                "teams": {"home": {"id": 1, "name": "A"}, "away": {"id": 2, "name": "B"}},
                "goals": {"home": null, "away": null}
              }]
            }
            """;

        mockServer.expect(requestTo("https://v3.football.api-sports.io/fixtures?id=1"))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        List<ExternalMatchData> result = provider.fetchMatchByExternalId(1L);

        assertEquals(MatchStatus.TO_BE_DEFINED, result.get(0).status());

        mockServer.verify();
    }
}