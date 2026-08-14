package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixturesStatusDto(
        @JsonProperty("long") String statusLong,
        @JsonProperty("short") String statusShort,
        Integer elapsed
) {
}
