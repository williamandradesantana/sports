package io.github.williamandradesantana.sports.interfaces.auth.dto;

import java.util.UUID;

public record RegisterResponse(UUID id, String username, String email, String fullName) {
}
