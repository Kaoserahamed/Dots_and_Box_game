package com.makesquare.server.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makesquare.server.auth.dto.ApiError;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Checks the caller's token before a request reaches a handler.
 *
 * <p>It is deny-by-default: a request to any endpoint that is not explicitly public has to
 * carry a valid token, or it is refused with a 401 before any handler runs. The endpoints that
 * are public are the ones a caller needs *in order to get a token*, plus the status endpoint
 * that says whether the server is up.
 *
 * <p>When the token is good, the account it belongs to is put on the request, so a handler
 * does not have to resolve it a second time.
 */
@Component
public class AuthMiddleware extends OncePerRequestFilter {

    /** The request attribute the authenticated account is stored under. */
    public static final String AUTHENTICATED_USER = "com.makesquare.auth.user";

    private static final String BEARER_PREFIX = "Bearer ";

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/status",
            "/error");

    private final AuthService authService;
    private final ObjectMapper objectMapper;

    /**
     * Create the middleware.
     *
     * @param authService the authentication service
     * @param objectMapper writes the refusal body
     */
    public AuthMiddleware(AuthService authService, ObjectMapper objectMapper) {
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (PUBLIC_PATHS.contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = bearerToken(request);
        if (token == null) {
            writeRefusal(response, "authentication_required", "A bearer token is required");
            return;
        }

        Optional<UserModel> user = authService.authenticate(token);
        if (user.isEmpty()) {
            writeRefusal(response, "invalid_token", "That token is not valid");
            return;
        }

        request.setAttribute(AUTHENTICATED_USER, user.get());
        filterChain.doFilter(request, response);
    }

    /**
     * Read the bearer token from a request.
     *
     * @param request the request to read
     * @return the token, or null when the request carries none
     */
    public static String bearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return null;
        }
        String token = header.substring(BEARER_PREFIX.length()).trim();
        return token.isEmpty() ? null : token;
    }

    private void writeRefusal(HttpServletResponse response, String code, String message)
            throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), new ApiError(code, message));
    }
}
