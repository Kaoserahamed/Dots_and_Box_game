package com.makesquare.server.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * The body of a login request.
 *
 * @param username the name the player signs in with
 * @param password the password as the player typed it
 */
public record LoginRequest(
        @NotBlank(message = "A username is required") String username,
        @NotBlank(message = "A password is required") String password) {
}
