package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixturesApiResponseTest {

    @Test
    @DisplayName("Test: deserializing a real API-Football fixture response should map all core fields")
    void test_DeserializingRealResponse_ShouldMapCoreFields() throws Exception {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String json = """
            {
              "response": [{
                "fixture": {
                  "id": 215662, "referee": "H. Mastrángelo", "date": "2019-10-20T14:00:00+00:00",
                  "venue": {"id": 33, "name": "Estadio José María Minella", "city": "Mar del Plata"},
                  "status": {"long": "Match Finished", "short": "FT", "elapsed": 90}
                },
                "league": {"id": 128, "name": "Liga Profesional Argentina", "country": "Argentina",
                           "season": 2019, "round": "Regular Season - 10"},
                "teams": {
                  "home": {"id": 463, "name": "Aldosivi", "winner": true},
                  "away": {"id": 442, "name": "Defensa Y Justicia", "winner": false}
                },
                "goals": {"home": 1, "away": 0}
              }]
            }
            """;

        FixturesApiResponse response = mapper.readValue(json, FixturesApiResponse.class);
        FixtureResponseItem item = response.response().get(0);

        assertEquals(215662L, item.fixture().id());
        assertEquals("FT", item.fixture().status().statusShort());
        assertEquals(128L, item.league().id());
        assertEquals(2019, item.league().season());
        assertEquals(463L, item.teams().home().id());
        assertEquals(1, item.goals().home());
    }
}