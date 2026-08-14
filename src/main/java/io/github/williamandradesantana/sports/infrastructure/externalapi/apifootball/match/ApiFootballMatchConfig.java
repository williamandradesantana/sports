package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match;

import io.github.williamandradesantana.sports.application.match.MatchProvider;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiFootballMatchConfig {

    @Bean
    public MatchProvider matchProvider(ApiFootballHttpClient httpClient) {
        return new ApiFootballMatchProvider(httpClient);
    }
}
