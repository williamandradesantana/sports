package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixtureStatisticsApiResponseTest {

    @Test
    @DisplayName("Test: deserializing statistics with mixed integer and percentage-string values")
    void test_DeserializingMixedValueTypes_ShouldSucceed() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = """
            {
              "response": [{
                "team": {"id": 463, "name": "Aldosivi"},
                "statistics": [
                  {"type": "Shots on Goal", "value": 3},
                  {"type": "Ball Possession", "value": "32%"},
                  {"type": "Red Cards", "value": null}
                ]
              }]
            }
            """;

        FixtureStatisticsApiResponse response = mapper.readValue(json, FixtureStatisticsApiResponse.class);
        FixtureStatisticsResponseItem item = response.response().get(0);

        assertEquals(3, item.statistics().get(0).value());
        assertEquals("32%", item.statistics().get(1).value());
        assertNull(item.statistics().get(2).value());
    }
}