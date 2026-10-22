package com.makesquare.server.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for the password hashing.
 */
class PasswordServiceTest {

    private final PasswordService passwordService = new PasswordService();

    @Test
    void aHashIsNotThePassword() {
        String hash = passwordService.hash("correct horse battery");

        assertNotEquals("correct horse battery", hash);
        assertFalse(hash.contains("correct horse battery"));
    }

    @Test
    void theRightPasswordVerifies() {
        String hash = passwordService.hash("correct horse battery");

        assertTrue(passwordService.matches("correct horse battery", hash));
    }

    @Test
    void aWrongPasswordDoesNotVerify() {
        String hash = passwordService.hash("correct horse battery");

        assertFalse(passwordService.matches("correct horse battery staple", hash));
    }

    @Test
    void twoHashesOfOnePasswordDiffer() {
        String first = passwordService.hash("correct horse battery");
        String second = passwordService.hash("correct horse battery");

        assertNotEquals(first, second);
    }

    @Test
    void anEmptyOrMissingHashNeverVerifies() {
        assertFalse(passwordService.matches("anything", ""));
        assertFalse(passwordService.matches("anything", null));
        assertFalse(passwordService.matches(null, "anything"));
    }
}
