package io.github.williamandradesantana.sports.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.util.Date;

public class JwtService {

    private final Algorithm algorithm;
    private final long expirationSeconds;

    public JwtService(JwtProperties properties) {
        this.algorithm = Algorithm.HMAC256(properties.secret());
        this.expirationSeconds = properties.expirationSeconds();
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        return JWT.create()
            .withSubject(username)
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(now.plusSeconds(expirationSeconds)))
            .sign(algorithm);
    }

    public String extractUsername(String token) {
        return verify(token).getSubject();
    }

    public boolean isValid(String token) {
        try {
            verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private DecodedJWT verify(String token) {
        return JWT.require(algorithm).build().verify(token);
    }
}
