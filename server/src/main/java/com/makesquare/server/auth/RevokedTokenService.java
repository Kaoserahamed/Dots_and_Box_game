package com.makesquare.server.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Remembers the tokens that have been handed back.
 *
 * <p>A token is normally left to expire on its own, but a player who signs out expects the
 * token to stop working at once. Revoked tokens are remembered by their digest rather than
 * by the token itself, so the store cannot be read to obtain a usable credential.
 */
@Component
public class RevokedTokenService {

    private final Set<String> revoked = ConcurrentHashMap.newKeySet();

    /**
     * Revoke a token.
     *
     * @param token the token that is being handed back
     */
    public void revoke(String token) {
        if (token != null && !token.isBlank()) {
            revoked.add(digestOf(token));
        }
    }

    /**
     * Say whether a token has been revoked.
     *
     * @param token the token to check
     * @return true when the token has been revoked
     */
    public boolean isRevoked(String token) {
        return token != null && revoked.contains(digestOf(token));
    }

    private String digestOf(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is required to revoke a token", e);
        }
    }
}
