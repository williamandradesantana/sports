package io.github.williamandradesantana.sports.application.user;

import java.util.Set;

public interface TokenService {
    String generateToken(String subject, Set<String> roles);
}