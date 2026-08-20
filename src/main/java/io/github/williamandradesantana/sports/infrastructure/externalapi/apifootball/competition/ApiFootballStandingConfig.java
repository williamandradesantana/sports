package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.competition;

import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiFootballStandingConfig {

    @Bean
    public ApiFootballStandingProvider apiFootballStandingProvider(ApiFootballHttpClient httpClient) {
        return new ApiFootballStandingProvider(httpClient);
    }
}
