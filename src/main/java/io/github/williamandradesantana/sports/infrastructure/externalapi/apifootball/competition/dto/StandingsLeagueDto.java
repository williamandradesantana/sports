package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StandingsLeagueDto(Long id, int season, List<List<StandingEntryDto>> standings) {
}
