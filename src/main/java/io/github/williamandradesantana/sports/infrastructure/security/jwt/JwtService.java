package io.github.williamandradesantana.sports.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.williamandradesantana.sports.application.user.TokenService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JwtService implements TokenService {

    private final Algorithm algorithm;
    private final long expirationSeconds;

    public JwtService(JwtProperties properties) {
        this.algorithm = Algorithm.HMAC256(properties.secret());
        this.expirationSeconds = properties.expirationSeconds();
    }

    @Override
    public String generateToken(String username, Set<String> roles) {
        Instant now = Instant.now();
        return JWT.create()
            .withSubject(username)
            .withClaim("roles", new ArrayList<>(roles))
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(now.plusSeconds(expirationSeconds)))
            .sign(algorithm);
    }

    public TokenClaims verifyAndExtractClaims(String token) {
        DecodedJWT decoded = JWT.require(algorithm).build().verify(token);
        Set<String> roles = new HashSet<>(decoded.getClaim("roles").asList(String.class));
        return new TokenClaims(decoded.getSubject(), roles);
    }
}
