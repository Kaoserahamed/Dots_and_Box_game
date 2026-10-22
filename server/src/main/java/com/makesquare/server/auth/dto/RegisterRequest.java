package com.makesquare.server.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * The body of a registration request.
 *
 * @param username the name the player signs in with
 * @param email the address the account is registered with
 * @param password the password, which is hashed before it is stored
 */
public record RegisterRequest(
        @NotBlank(message = "A username is required")
        @Size(min = 3, max = 32, message = "A username must be between 3 and 32 characters")
        String username,

        @NotBlank(message = "An email address is required")
        @Email(message = "That does not look like an email address")
        @Size(max = 254, message = "An email address cannot be longer than 254 characters")
        String email,

        @NotBlank(message = "A password is required")
        @Size(min = 8, max = 128, message = "A password must be between 8 and 128 characters")
        String password) {
}
