package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.team.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TeamResponseItem(TeamDto team, VenueDto venue) {
}
