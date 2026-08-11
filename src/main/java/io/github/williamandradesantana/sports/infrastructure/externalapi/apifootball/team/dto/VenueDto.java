package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.team.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VenueDto(
        Long id, String name, String address, String city,
        Integer capacity, String surface, String image
) {
}
