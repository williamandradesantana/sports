package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FixtureDto(
        Long id, String referee, OffsetDateTime date,  FixtureVenueDto venue, FixturesStatusDto status
) {
}
