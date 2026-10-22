package com.makesquare.server.auth;

import com.makesquare.server.auth.dto.AuthResponse;
import com.makesquare.server.auth.dto.LoginRequest;
import com.makesquare.server.auth.dto.RegisterRequest;
import com.makesquare.server.auth.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The authentication endpoints.
 *
 * <p>Registration and sign in are the only two endpoints that do not need a token. Everything
 * else is behind the {@link AuthMiddleware}, which checks the token before the request reaches
 * a handler.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Create the controller.
     *
     * @param authService the authentication service
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register an account and sign it in.
     *
     * @param request the account to create
     * @return the account and a session token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    /**
     * Sign an account in.
     *
     * @param request the name and the password
     * @return the account and a session token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Return the account the caller is signed in as.
     *
     * @param request the current request, carrying the authenticated account
     * @return the account
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(HttpServletRequest request) {
        UserModel user = (UserModel) request.getAttribute(AuthMiddleware.AUTHENTICATED_USER);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    /**
     * Sign the caller out.
     *
     * <p>The token is revoked at once rather than being left to expire, so a token that has
     * been handed back stops working straight away.
     *
     * @param request the current request, carrying the bearer token
     * @return an empty body
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        authService.logout(AuthMiddleware.bearerToken(request));
        return ResponseEntity.noContent().build();
    }
}
