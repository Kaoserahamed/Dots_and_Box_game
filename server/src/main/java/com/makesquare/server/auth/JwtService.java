package com.makesquare.server.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Issues and verifies the session tokens.
 *
 * <p>A token carries the account's identifier, its name and its role, and it is signed with
 * HMAC-SHA256. The signature is what makes the contents trustworthy: the server can read who the
 * caller is without a database round trip, and a client cannot invent a token for an account it
 * does not own.
 */
@Service
public class JwtService {

    private final SecretKey signingKey;
    private final Duration tokenLifetime;

    /**
     * Create the service.
     *
     * @param secret the signing secret, at least 32 bytes so that HMAC-SHA256 is used
     * @param expirationMinutes how long a token stays valid
     */
    public JwtService(
            @Value("${auth.jwt.secret}") String secret,
            @Value("${auth.jwt.expiration-minutes:60}") long expirationMinutes) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenLifetime = Duration.ofMinutes(expirationMinutes);
    }

    /**
     * Issue a token for an account.
     *
     * @param user the account the token is for
     * @return the signed token
     */
    public String issue(UserModel user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(Long.toString(user.id()))
                .claim("username", user.username())
                .claim("role", user.role())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(tokenLifetime)))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Read the claims of a token.
     *
     * @param token the token to read
     * @return the claims, or empty when the token is malformed, unsigned by this server or expired
     */
    public Optional<Claims> parse(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Say how long a token stays valid.
     *
     * @return the lifetime of a token
     */
    public Duration tokenLifetime() {
        return tokenLifetime;
    }
}
