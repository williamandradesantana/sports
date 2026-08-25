package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OddsResponseItem(OddsFixtureRefDto fixture, OffsetDateTime update, List<OddsBookmakerDto> bookmakers) {
}
