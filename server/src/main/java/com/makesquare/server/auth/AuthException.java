package com.makesquare.server.auth;

/**
 * A request the authentication service refused.
 *
 * <p>The message is written for the player who made the request, and the code is written for
 * the code that reads it. Neither ever carries a password or a token.
 */
public class AuthException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    /**
     * Record the refusal.
     *
     * @param code a stable, machine-readable identifier for the failure
     * @param message a human-readable explanation
     */
    public AuthException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Return the machine-readable identifier for the failure.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }
}
