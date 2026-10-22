package com.makesquare.server.auth;

import com.makesquare.server.auth.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Turns the authentication refusals into the same shape as every other error the API returns.
 *
 * <p>Without this, a refused registration would come back as a stack trace and a refused
 * sign in as a plain message, and a client would have to know which is which.
 */
@RestControllerAdvice
public class AuthExceptionHandler {

    /**
     * Report a refusal the authentication service made.
     *
     * @param exception the refusal
     * @return the error body
     */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiError> handleAuthException(AuthException exception) {
        HttpStatus status = switch (exception.getCode()) {
            case "username_taken", "email_taken" -> HttpStatus.CONFLICT;
            case "invalid_credentials" -> HttpStatus.UNAUTHORIZED;
            default -> HttpStatus.BAD_REQUEST;
        };
        return ResponseEntity.status(status)
                .body(new ApiError(exception.getCode(), exception.getMessage()));
    }
}
