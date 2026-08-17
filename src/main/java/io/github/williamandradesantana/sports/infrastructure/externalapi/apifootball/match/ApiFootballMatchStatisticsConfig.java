package io.github.williamandradesantana.sports.infrastructure.externalapi.apifootball.match;

import io.github.williamandradesantana.sports.application.match.MatchStatisticsProvider;
import io.github.williamandradesantana.sports.infrastructure.shared.apifootball.ApiFootballHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiFootballMatchStatisticsConfig {

    @Bean
    public MatchStatisticsProvider matchStatisticsProvider(ApiFootballHttpClient httpClient) {
        return new ApiFootballMatchStatisticsProvider(httpClient);
    }
}
