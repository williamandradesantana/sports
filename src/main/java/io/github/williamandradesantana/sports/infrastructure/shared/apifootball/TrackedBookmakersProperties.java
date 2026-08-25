package io.github.williamandradesantana.sports.infrastructure.shared.apifootball;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.bookmakers")
public record TrackedBookmakersProperties(List<Long> externalIds) {
}
