package io.github.williamandradesantana.sports.infrastructure.security.jwt;

import java.util.Set;

public record TokenClaims(String username, Set<String> roles) {
}
