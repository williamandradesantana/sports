package io.github.williamandradesantana.sports.application.user;

public record LoginCommand(String username, String rawPassword) {
}
