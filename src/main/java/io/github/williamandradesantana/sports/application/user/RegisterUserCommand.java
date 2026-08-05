package io.github.williamandradesantana.sports.application.user;

public record RegisterUserCommand(String username, String fullName, String email, String rawPassword) {
}
