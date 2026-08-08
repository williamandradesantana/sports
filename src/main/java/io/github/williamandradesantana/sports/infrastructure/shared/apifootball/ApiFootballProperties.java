package io.github.williamandradesantana.sports.infrastructure.shared.apifootball;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api-football")
public record ApiFootballProperties(String baseUrl, String apiKey) {
}
