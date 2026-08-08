package io.github.williamandradesantana.sports.infrastructure.shared.apifootball;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiFootballProperties.class)
public class ApiFootballConfig {

    @Bean
    public ApiFootballHttpClient apiFootballHttpClient(ApiFootballProperties properties) {
        return new ApiFootballHttpClient(properties);
    }
}
