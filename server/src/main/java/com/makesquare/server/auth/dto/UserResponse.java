package com.makesquare.server.auth.dto;

import com.makesquare.server.auth.UserModel;
import java.time.Instant;

/**
 * An account as the API returns it.
 *
 * <p>The password hash is deliberately absent: the API has no reason to hand it out, and an
 * account that leaves the server should not carry it.
 *
 * @param id the identifier of the account
 * @param username the name the player signs in with
 * @param email the address the account was registered with
 * @param role the role the account holds
 * @param createdAt when the account was created
 */
public record UserResponse(
        long id,
        String username,
        String email,
        String role,
        Instant createdAt) {

    /**
     * Build the response for an account.
     *
     * @param user the account
     * @return the response
     */
    public static UserResponse from(UserModel user) {
        return new UserResponse(
                user.id(),
                user.username(),
                user.email(),
                user.role(),
                user.createdAt());
    }
}
