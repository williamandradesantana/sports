package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.league;

import io.github.williamandradesantana.sports.application.league.LeagueProvider;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiFootballLeagueConfig {

    @Bean
    public LeagueProvider leagueProvider(ApiFootballHttpClient httpClient) {
        return new ApiFootballLeagueProvider(httpClient);
    }
}
