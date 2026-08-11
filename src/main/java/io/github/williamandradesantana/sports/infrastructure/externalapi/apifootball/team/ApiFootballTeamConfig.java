package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.team;

import io.github.williamandradesantana.sports.application.team.TeamProvider;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiFootballTeamConfig {

    @Bean
    public TeamProvider teamProvider(ApiFootballHttpClient httpClient) {
        return new ApiFootballTeamProvider(httpClient);
    }
}
