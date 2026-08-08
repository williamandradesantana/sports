package io.github.williamandradesantana.sports.infrastructure.shared.apifootball;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeagueResponseItem(LeagueDto league, CountryDto country, List<SeasonDto> seasons) {
}
