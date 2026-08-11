package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.team.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeamsApiResponseTest {

    @Test
    @DisplayName("Test: deserializing a real API-Football teams response should map all fields correctly")
    void test_DeserializingRealResponse_ShouldMapAllFields() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
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

        TeamsApiResponse response = mapper.readValue(json, TeamsApiResponse.class);

        TeamResponseItem item = response.response().get(0);
        assertEquals(33L, item.team().id());
        assertEquals("Manchester United", item.team().name());
        assertEquals(556L, item.venue().id());
        assertEquals(76212, item.venue().capacity());
    }

    @Test
    @DisplayName("Test: deserializing a national team without a venue field should leave venue null")
    void test_DeserializingNationalTeamWithoutVenue_ShouldLeaveVenueNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = """
            {
              "response": [{
                "team": {"id": 10, "name": "Brazil", "code": "BRA", "country": "Brazil",
                         "founded": null, "national": true, "logo": "https://logo.png"}
              }]
            }
            """;

        TeamsApiResponse response = mapper.readValue(json, TeamsApiResponse.class);

        assertNull(response.response().get(0).venue());
    }
}