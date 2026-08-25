package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OddsApiResponseTest {

    @Test
    @DisplayName("Test: deserializing a real API-Football odds response should map bookmakers and bets")
    void test_DeserializingRealResponse_ShouldMapBookmakersAndBets() throws Exception {
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String json = """
            {
              "response": [{
                "fixture": {"id": 1490391},
                "update": "2026-08-19T22:00:00+00:00",
                "bookmakers": [{
                  "id": 8, "name": "Bet365",
                  "bets": [{
                    "id": 1, "name": "Match Winner",
                    "values": [
                      {"value": "Home", "odd": "2.40"},
                      {"value": "Draw", "odd": "3.90"},
                      {"value": "Away", "odd": "2.62"}
                    ]
                  }]
                }]
              }]
            }
            """;

        OddsApiResponse response = mapper.readValue(json, OddsApiResponse.class);
        OddsResponseItem item = response.response().get(0);
        OddsBookmakerDto bookmaker = item.bookmakers().get(0);

        assertEquals(1490391L, item.fixture().id());
        assertEquals("Bet365", bookmaker.name());
        assertEquals(1, bookmaker.bets().get(0).id());
        assertEquals("2.40", bookmaker.bets().get(0).values().get(0).odd());
    }
}