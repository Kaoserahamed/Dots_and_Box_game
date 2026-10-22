package com.makesquare.server.auth;

import java.time.Instant;

/**
 * An account.
 *
 * <p>A record, because an account is a value: it is read and never mutated in place. The
 * password hash is part of it, which is why it never appears in a response - the response is
 * built from {@link com.makesquare.server.auth.dto.UserResponse} instead.
 *
 * @param id the identifier of the account
 * @param username the name the player signs in with
 * @param email the address the account was registered with
 * @param passwordHash the hashed password, never the password itself
 * @param role the role the account holds
 * @param createdAt when the account was created
 */
public record UserModel(
        long id,
        String username,
        String email,
        String passwordHash,
        String role,
        Instant createdAt) {
}
