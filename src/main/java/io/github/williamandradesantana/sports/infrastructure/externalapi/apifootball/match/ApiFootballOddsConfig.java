package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match;

import io.github.williamandradesantana.sports.application.match.OddsProvider;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiFootballOddsConfig {

    @Bean
    public OddsProvider oddsProvider(ApiFootballHttpClient httpClient) {
        return new ApiFootballOddsProvider(httpClient);
    }
}
