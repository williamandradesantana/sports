package io.github.williamandradesantana.sports.infrastructure.shared.apifootball;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaguesApiResponseTest {

    @Test
    @DisplayName("Test: deserializing a real API-Football leagues response should map all fields correctly")
    void test_DeserializingRealResponse_ShouldMapAllFields() throws Exception {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
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

        LeaguesApiResponse response = mapper.readValue(json, LeaguesApiResponse.class);

        assertEquals(1, response.response().size());
        LeagueResponseItem item = response.response().get(0);
        assertEquals(39L, item.league().id());
        assertEquals("Premier League", item.league().name());
        assertEquals("England", item.country().name());
        assertEquals(2026, item.seasons().get(0).year());
        assertTrue(item.seasons().get(0).current());
        assertTrue(item.seasons().get(0).coverage().standings());
    }
}