package com.makesquare.server.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Hashes and checks passwords.
 *
 * <p>bcrypt is used rather than a plain digest, because a password is a low-entropy secret that
 * somebody might try to guess. bcrypt is deliberately slow to compute, and it salts every
 * password, so two accounts with the same password get different hashes and a stolen hash is
 * expensive to attack.
 */
@Component
public class PasswordService {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Hash a password.
     *
     * @param rawPassword the password as the player typed it
     * @return the hash to store
     */
    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * Check a password against a stored hash.
     *
     * @param rawPassword the password as the player typed it
     * @param passwordHash the hash that was stored
     * @return true when the password is the one the hash stands for
     */
    public boolean matches(String rawPassword, String passwordHash) {
        if (rawPassword == null || passwordHash == null || passwordHash.isEmpty()) {
            return false;
        }
        return encoder.matches(rawPassword, passwordHash);
    }
}
