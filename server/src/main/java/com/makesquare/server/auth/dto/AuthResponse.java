package com.makesquare.server.auth.dto;

import java.time.Instant;

/**
 * The answer to a successful registration or sign in.
 *
 * @param user the account that was authenticated
 * @param token the session token to send back with every request
 * @param expiresAt when the token stops being accepted
 */
public record AuthResponse(UserResponse user, String token, Instant expiresAt) {
}
