package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandingsApiResponseTest {

    @Test
    @DisplayName("Test: deserializing a real API-Football standings response should map all fields")
    void test_DeserializingRealResponse_ShouldMapAllFields() throws Exception {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String json = """
            {
              "response": [{
                "league": {
                  "id": 71, "season": 2024,
                  "standings": [[{
                    "rank": 1,
                    "team": {"id": 120, "name": "Botafogo", "logo": "https://logo.png"},
                    "points": 79, "goalsDiff": 30, "group": "Serie A", "form": "WWWDD",
                    "status": "same", "description": "CONMEBOL Libertadores",
                    "all": {"played": 38, "win": 23, "draw": 10, "lose": 5, "goals": {"for": 59, "against": 29}},
                    "home": {"played": 19, "win": 12, "draw": 5, "lose": 2, "goals": {"for": 31, "against": 13}},
                    "away": {"played": 19, "win": 11, "draw": 5, "lose": 3, "goals": {"for": 28, "against": 16}},
                    "update": "2024-12-11T00:00:00+00:00"
                  }]]
                }
              }]
            }
            """;

        StandingsApiResponse response = mapper.readValue(json, StandingsApiResponse.class);
        StandingEntryDto entry = response.response().get(0).league().standings().get(0).get(0);

        assertEquals(1, entry.rank());
        assertEquals(120L, entry.team().id());
        assertEquals(59, entry.all().goals().goalsFor());
        assertEquals(29, entry.all().goals().against());
        assertEquals("same", entry.status());
    }
}