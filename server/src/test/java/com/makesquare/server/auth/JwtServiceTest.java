package com.makesquare.server.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Claims;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Tests for the session tokens.
 */
class JwtServiceTest {

    private static final String SECRET = "a-test-signing-secret-with-enough-bytes";

    private final JwtService jwtService = new JwtService(SECRET, 60);

    private UserModel aUser() {
        return new UserModel(7L, "gopal", "gopal@example.com", "hash", "PLAYER", Instant.now());
    }

    @Test
    void aTokenCarriesTheAccount() {
        String token = jwtService.issue(aUser());

        Optional<Claims> claims = jwtService.parse(token);

        assertTrue(claims.isPresent());
        assertEquals("7", claims.get().getSubject());
        assertEquals("gopal", claims.get().get("username", String.class));
        assertEquals("PLAYER", claims.get().get("role", String.class));
    }

    @Test
    void aTamperedTokenIsRefused() {
        String token = jwtService.issue(aUser());
        String tampered = token.substring(0, token.length() - 4) + "AAAA";

        assertTrue(jwtService.parse(tampered).isEmpty());
    }

    @Test
    void aTokenSignedWithAnotherSecretIsRefused() {
        String token = new JwtService("a-different-secret-with-enough-bytes", 60).issue(aUser());

        assertTrue(jwtService.parse(token).isEmpty());
    }

    @Test
    void aMalformedTokenIsRefused() {
        assertTrue(jwtService.parse("not-a-token").isEmpty());
        assertTrue(jwtService.parse("").isEmpty());
        assertTrue(jwtService.parse(null).isEmpty());
    }

    @Test
    void theLifetimeIsTheOneThatWasConfigured() {
        assertEquals(60, jwtService.tokenLifetime().toMinutes());
    }
}
