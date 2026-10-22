package com.makesquare.server.auth;

import com.makesquare.server.auth.dto.AuthResponse;
import com.makesquare.server.auth.dto.LoginRequest;
import com.makesquare.server.auth.dto.RegisterRequest;
import com.makesquare.server.auth.dto.UserResponse;
import io.jsonwebtoken.Claims;
import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Registration, sign in and the rules that go with them.
 *
 * <p>The rules are deliberately blunt:
 *
 * <ul>
 *   <li>A name and an address can each be taken only once.
 *   <li>A password is never stored, only its hash.
 *   <li>A wrong name and a wrong password produce the same answer, so the endpoint cannot be
 *       used to find out which names have accounts.
 *   <li>Signing out revokes the token at once rather than waiting for it to expire.
 * </ul>
 */
@Service
public class AuthService {

    /** The role a newly registered account holds. */
    public static final String DEFAULT_ROLE = "PLAYER";

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final JwtService jwtService;
    private final RevokedTokenService revokedTokenService;

    /**
     * Create the service.
     *
     * @param userRepository where accounts are kept
     * @param passwordService hashes and checks passwords
     * @param jwtService issues and verifies the session tokens
     * @param revokedTokenService remembers the tokens that have been handed back
     */
    public AuthService(
            UserRepository userRepository,
            PasswordService passwordService,
            JwtService jwtService,
            RevokedTokenService revokedTokenService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.jwtService = jwtService;
        this.revokedTokenService = revokedTokenService;
    }

    /**
     * Register an account and sign it in.
     *
     * @param request the account to create
     * @return the account and a session token
     * @throws AuthException when the name or the address is already taken
     */
    public AuthResponse register(RegisterRequest request) {
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByUsername(username)) {
            throw new AuthException("username_taken", "That username is already registered");
        }
        if (userRepository.existsByEmail(email)) {
            throw new AuthException("email_taken", "That email address is already registered");
        }

        UserModel user = userRepository.create(
                username,
                email,
                passwordService.hash(request.password()),
                DEFAULT_ROLE);
        return toResponse(user);
    }

    /**
     * Sign an account in.
     *
     * @param request the name and the password
     * @return the account and a session token
     * @throws AuthException when the name or the password is not right
     */
    public AuthResponse login(LoginRequest request) {
        Optional<UserModel> found = userRepository.findByUsername(request.username().trim());
        if (found.isEmpty() || !passwordService.matches(request.password(), found.get().passwordHash())) {
            throw new AuthException("invalid_credentials", "That username or password is not right");
        }
        return toResponse(found.get());
    }

    /**
     * Resolve a token into the account it belongs to.
     *
     * @param token the session token
     * @return the account, or empty when the token is not valid
     */
    public Optional<UserModel> authenticate(String token) {
        if (token == null || token.isBlank() || revokedTokenService.isRevoked(token)) {
            return Optional.empty();
        }
        Optional<Claims> claims = jwtService.parse(token);
        if (claims.isEmpty()) {
            return Optional.empty();
        }
        try {
            long id = Long.parseLong(claims.get().getSubject());
            return userRepository.findById(id);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Sign an account out.
     *
     * @param token the session token being handed back
     */
    public void logout(String token) {
        revokedTokenService.revoke(token);
    }

    /**
     * Say how long a token stays valid.
     *
     * @return the lifetime of a token
     */
    public Instant tokenExpiry() {
        return Instant.now().plus(jwtService.tokenLifetime());
    }

    private AuthResponse toResponse(UserModel user) {
        return new AuthResponse(
                UserResponse.from(user),
                jwtService.issue(user),
                tokenExpiry());
    }
}
