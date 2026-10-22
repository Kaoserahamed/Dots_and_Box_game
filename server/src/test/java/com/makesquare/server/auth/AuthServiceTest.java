package com.makesquare.server.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.makesquare.server.auth.dto.AuthResponse;
import com.makesquare.server.auth.dto.LoginRequest;
import com.makesquare.server.auth.dto.RegisterRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the authentication rules.
 */
class AuthServiceTest {

    private static final String SECRET = "a-test-signing-secret-with-enough-bytes";

    private InMemoryUserRepository userRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        authService = new AuthService(
                userRepository,
                new PasswordService(),
                new JwtService(SECRET, 60),
                new RevokedTokenService());
    }

    private AuthResponse register(String username, String email) {
        return authService.register(new RegisterRequest(username, email, "correct-horse-battery"));
    }

    @Test
    void registeringCreatesAnAccount() {
        AuthResponse response = register("gopal", "gopal@example.com");

        assertEquals("gopal", response.user().username());
        assertEquals("gopal@example.com", response.user().email());
        assertEquals(AuthService.DEFAULT_ROLE, response.user().role());
        assertTrue(response.token() != null && !response.token().isEmpty());
    }

    @Test
    void aRegisteredAccountNeverCarriesItsPassword() {
        AuthResponse response = register("gopal", "gopal@example.com");

        Optional<UserModel> stored = userRepository.findByUsername("gopal");
        assertTrue(stored.isPresent());
        assertNotEquals("correct-horse-battery", stored.get().passwordHash());
    }

    @Test
    void aUsernameCanOnlyBeTakenOnce() {
        register("gopal", "gopal@example.com");

        AuthException refusal = assertThrows(
                AuthException.class, () -> register("gopal", "other@example.com"));
        assertEquals("username_taken", refusal.getCode());
    }

    @Test
    void anEmailCanOnlyBeRegisteredOnce() {
        register("gopal", "gopal@example.com");

        AuthException refusal = assertThrows(
                AuthException.class, () -> register("madhav", "gopal@example.com"));
        assertEquals("email_taken", refusal.getCode());
    }

    @Test
    void signingInWithTheRightPasswordWorks() {
        register("gopal", "gopal@example.com");

        AuthResponse response = authService.login(new LoginRequest("gopal", "correct-horse-battery"));

        assertEquals("gopal", response.user().username());
        assertTrue(response.token() != null && !response.token().isEmpty());
    }

    @Test
    void signingInWithTheWrongPasswordIsRefused() {
        register("gopal", "gopal@example.com");

        AuthException refusal = assertThrows(
                AuthException.class,
                () -> authService.login(new LoginRequest("gopal", "wrong-horse-battery")));
        assertEquals("invalid_credentials", refusal.getCode());
    }

    @Test
    void signingInWithAnUnknownNameIsRefusedTheSameWay() {
        register("gopal", "gopal@example.com");

        AuthException refusal = assertThrows(
                AuthException.class,
                () -> authService.login(new LoginRequest("nobody", "correct-horse-battery")));
        assertEquals("invalid_credentials", refusal.getCode());
    }

    @Test
    void aTokenResolvesToItsAccount() {
        AuthResponse response = register("gopal", "gopal@example.com");

        Optional<UserModel> user = authService.authenticate(response.token());

        assertTrue(user.isPresent());
        assertEquals("gopal", user.get().username());
    }

    @Test
    void signingOutRevokesTheTokenAtOnce() {
        AuthResponse response = register("gopal", "gopal@example.com");

        authService.logout(response.token());

        assertTrue(authService.authenticate(response.token()).isEmpty());
    }

    @Test
    void aTokenThatWasNeverIssuedIsRefused() {
        assertTrue(authService.authenticate("not-a-token").isEmpty());
        assertTrue(authService.authenticate(null).isEmpty());
    }

    @Test
    void aRevokedTokenDoesNotRevokeTheAccount() {
        AuthResponse response = register("gopal", "gopal@example.com");
        authService.logout(response.token());

        assertFalse(userRepository.findByUsername("gopal").isEmpty());
    }
}
